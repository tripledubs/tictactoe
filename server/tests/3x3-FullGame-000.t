#!/usr/bin/env perl
use strict;
use warnings;
use Carp;
use URI;
use Data::Dumper;
use LWP;
use Data::Dump;
use Test::More;

my $ua = LWP::UserAgent->new;
my $link = URI->new('http://cs2.uco.edu/~gq011/tictactoe/server/');

sub interactServer {
	my ($method, %params) = @_;
	$link->query_form(controller=>'api', method => $method, %params);
	my $request = HTTP::Request->new(GET => $link);
	my $response = $ua->request($request);
	return $response->decoded_content;
}

sub makeMove {
	my ($game, $player, $position) = @_;
	$link->query_form(
		controller => 'api',
		method => 'move',
		gameid => $game,
	 	playerid => $player,
	 	position => $position
	);
	my $response = $ua->request(HTTP::Request->new(POST => $link));
	return $response->decoded_content;
}

my $gameID = interactServer('start',boardSize => 3);
my $player1 = interactServer('connect',gameid=>$gameID);
my $player2 = interactServer('connect',gameid=>$gameID);
my $gameStatus = interactServer('status', gameid => $gameID);

is(length($gameID),4,"GameID is 4 characters");
is(length($player1), 32, "Player1 is 32 character long string");
is(length($player2), 32, "Player1 is 32 character long string");
is($gameStatus,1,"First players turn to move");

my $move = makeMove($gameID, $player1, 0);
is($gameStatus,2,"Second players turn to move");


# X O X
# 0 X 0
# X  



done_testing();
