<?php 
	//this prevents someone from bypassing the index.php file and going directly here
	if(!defined('_EXEC'))
	{
		die('Direct access not premitted');
	}
	
	//timezone
	date_default_timezone_set('America/Chicago');
	
	//Error reporting
	error_reporting(0);
	ini_set("display_errors", 0);
	//error_reporting(E_ALL);
	//ini_set("display_errors", 1);
	
	//Base URL
	define('URL', 'https://172.16.142.60/dev/tictactoe/server/');
	
	//Paths
	define('LIBS_PATH', 'lib/');
	define('CONTROLLER_PATH', 'controllers/');
	define('MODELS_PATH', 'models/');
	define('VIEWS_PATH', 'views/');
	
	//Database
	define('DB_TYPE', 'mysql');
	//define('DB_USER', 'gq011');
	//define('DB_PASS', '#######');
	define('DB_HOST', '127.0.0.1');
	define('DB_NAME', 'tictactoe');
define('DB_USER', 'soc_access');
define('DB_PASS', '83KsxEzjPu7tATqT');
	
	// Messages
	define('MESSAGE_UNKNOWN_ERROR', 'Unknown Error');
	define('MESSAGE_DATABASE_ERROR', 'Database Error');
	define('MESSAGE_INVALID_COMMAND', 'Invalid Move/Command');
	define('MESSAGE_MISSING_GAMEID', 'You must provide a valid game ID');
	define('MESSAGE_GAME_FULL', 'You cant join this game as it is already full');
	define('MESSAGE_GAME_NOT_FOUND', 'That game does not exist');
	define('MESSAGE_MISSING_PLAYERID', 'You must provide a valid player ID');
	define('MESSAGE_PLAYER_NOT_IN_GAME', 'Player is not part of this game');
	define('MESSAGE_GAME_NOT_STARTED', 'Game not started');
	define('MESSAGE_NOT_YOUR_TURN', 'It is not your turn');
	define('MESSAGE_GAME_OVER', 'Game is over');
