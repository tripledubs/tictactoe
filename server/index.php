<?php

//set _EXEC to true so files can check aginst direct access, each view will die() if this is not set first
//this prevents direct access to php files
define('_EXEC', TRUE);

require 'config/config.php';
require 'lib/Application.php';
require 'lib/Controller.php';
require 'lib/View.php';

//allow ajax calls from other domains
header('Access-Control-Allow-Origin: *');


// Start the application
$app = new Application();