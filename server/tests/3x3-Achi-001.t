#!/usr/bin/env perl
use strict;
use warnings;
use lib '.';
use Carp;
use URI;
use LWP;
use Test::More;
use tttt;
use JSON;

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
   if ($response->decoded_content eq '0') {
       BAIL_OUT("Server refused move!");
   }
   return $response->decoded_content;
}

sub getGrid {
    my ($game) = @_;
    my $json = interactServer('grid',gameid => $game);
    return $json;
}

my $gameID;
my $player1;
my $player2;
my $gameStatus;

# $winner should match output from server
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
    is(interactServer('status',gameid => $gameID),$winner,"Status == $winner");
    return $gameID;
}
testGame(3,4,0,1,2,7);
testGame(3,4,1,2,3,6);
testGame(4,0,1,2,4,5,7);
testGame(2,0,0,0,0,0,0); # Move 2+ should not be accepted, still player ones turn
testGame(1); # Move 3+ should not be accepted, still player ones turn
testGame(2,1);
testGame(1,1,2);
testGame(2,1,2,3);
testGame(2,1,2,3,3); #Last move should not be accepted
testGame(2,1,2,3,3,3); #Last move should not be accepted
testGame(1,1,2,3,3,3,4); #Last move should be accepted
print getGrid(testGame(1,4,2,5,3,0,8,7,1)); # Should be in achi mode now
print getGrid(testGame(2,4,2,5,3,0,8,7,1,7)); # x Slides left
print getGrid(testGame(1,4,2,5,3,0,8,7,1,7,8)); # 0 Slides left
print getGrid(testGame(3,4,2,5,3,0,8,7,1,7,8,5)); # X Slides Down for the win
done_testing();
