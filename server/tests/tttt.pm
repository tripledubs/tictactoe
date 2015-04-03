#!/usr/bin/env perl
package tttt; # Tic tac toe tests
use strict;
use warnings;
use File::Basename;
use Exporter 'import';
our @EXPORT = 'server';

my $installDir = '/var/www/ttt/seans';
sub server () {
	open (my $file, "$installDir/server/config/config.php") or die $!;
	my ($server) = map { /(http[^']*)/ } <$file>;
}
