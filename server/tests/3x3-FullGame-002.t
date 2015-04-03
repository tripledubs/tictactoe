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

my $gameID     = interactServer( 'start',   boardSize => 3 );
my $player1    = interactServer( 'connect', gameid    => $gameID );
my $player2    = interactServer( 'connect', gameid    => $gameID );
my $gameStatus = interactServer( 'status',  gameid    => $gameID );

is( length($gameID),  4,  "GameID is 4 characters" );
is( length($player1), 32, "Player1 is 32 character long string" );
is( length($player2), 32, "Player1 is 32 character long string" );
is( $gameStatus,      1,  "First players turn to move" );

my $move = makeMove( $gameID, $player1, '1' );
$gameStatus = interactServer( 'status', gameid => $gameID );
is( $gameStatus, 2, "Second players turn to move" );

my @game = ( 0, 1, 2, 4, 5, );

# The first 6 turns..
my $player1sTurn = 1;
for (@game) {
   my $player = ( $player1sTurn == 1 ) ? $player1 : $player2;
   makeMove( $gameID, $player, $_ );
   $player1sTurn = ( $player eq $player1 ) ? 2 : 1;
   is( interactServer( 'status', gameid => $gameID ),
      $player1sTurn, "Moved $_, changed turns.." );
}

is( makeMove( $gameID, $player2, 7 ), 1, "Player 2 final move" );
is( interactServer( 'status', gameid => $gameID ), 4,
   "Second player has won!" );

# test 1
# X O X
# 0 X 0
# X

# Test 2
# O _ X
# O X X
# O _ _

# Test 3
# X O X
#   O X
# X 0 _

done_testing();
