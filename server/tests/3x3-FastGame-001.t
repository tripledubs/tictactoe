#!/usr/bin/env perl
use strict;
use warnings;
use lib '.';
use Carp;
use URI;
use LWP;
use Test::More;
use tttt;

my $ua = LWP::UserAgent->new;
$ua->timeout(1);
my $link = URI->new(server);

sub interactServer {
   my ( $method, %params ) = @_;
   $link->query_form( controller => 'api', method => $method, %params );
   my $request = HTTP::Request->new( GET => $link );
   my $response = $ua->request($request);
   return $response->decoded_content;
}

sub makeMove {
   my ( $game, $player, $position ) = @_;
   $link->query_form(
      controller => 'api',
      method     => 'move',
      gameid     => $game,
      playerid   => $player,
      position   => $position
   );
   my $response = $ua->request( HTTP::Request->new( POST => $link ) );
   return $response->decoded_content;
}

my $gameID;
my $player1;
my $player2;
my $gameStatus;

sub testGame {
    my ($winner,@moves) = @_;
    my $playersTurn = 0;
    $gameID     = interactServer( 'start',   boardSize => 3 );
    $player1    = interactServer( 'connect', gameid    => $gameID );
    $player2    = interactServer( 'connect', gameid    => $gameID );
    $gameStatus = interactServer( 'status',  gameid    => $gameID );
    for (@moves) {
        makeMove($gameID, $playersTurn ? $player2 : $player1, $_);
        $playersTurn = ! $playersTurn;
    }
    is(interactServer('status',gameid => $gameID),$winner,"$winner is expected");
}
testGame(3,4,0,1,2,7);
testGame(3,4,1,2,3,6);
testGame(4,0,1,2,4,5,7);

done_testing();
