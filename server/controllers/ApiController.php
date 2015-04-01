<?php 
	//this prevents someone from bypassing the index.php file and going directly here
	if(!defined('_EXEC'))
	{
		header("HTTP/1.1 400 Bad Request");
		die('Direct access not premitted');
	}



class API extends Controller
{
	public function __construct()
	{
		parent::__construct();
	}
	
	//display the apis page
	function index(){
		$this->view->render('api/index');
	}
	
	//starts a new game
	function start(){
		
		//get model
		require MODELS_PATH . 'ApiModel.php';
		$model = new ApiModel($this->db);
		
		//process request
		$gameId = $model->start($boardSize);
		
		//return
		echo $gameId;
	}
	
	//join player to game
	function connect(){
		//grab input
		if(isset($_REQUEST['gameid'])){
			$gameId = $_REQUEST['gameid'];
		}
		else {
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_MISSING_GAMEID);
		}
	
		//get model
		require MODELS_PATH . 'ApiModel.php';
		$model = new ApiModel($this->db);
	
		//process request
		$playerId = $model->connect($gameId);
	
		//return
		header("HTTP/1.1 200 OK");
		echo $playerId;
	}
	
	//get status code
	function status(){
		//grab input
		if(isset($_REQUEST['gameid'])){
			$gameId = $_REQUEST['gameid'];
		}
		else {
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_MISSING_GAMEID);
		}
	
		//get model
		require MODELS_PATH . 'ApiModel.php';
		$model = new ApiModel($this->db);
	
		//process request
		$status = $model->status($gameId);
		echo $status;
// 		if(isset($_REQUEST['longpolling'])){
// 			$newstatus = $model->status($gameId);
// 			while($status == $newstatus && $newstatus != 3 && $newstatus != 4){
// 				$newstatus = $model->status($gameId);
// 			}
// 			echo $newstatus;
// 		}
// 		else{
// 			echo $status;
// 		}
	}
	
	//get mode
	function mode(){
		//grab input
		if(isset($_REQUEST['gameid'])){
			$gameId = $_REQUEST['gameid'];
		}
		else {
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_MISSING_GAMEID);
		}
	
		//get model
		require MODELS_PATH . 'ApiModel.php';
		$model = new ApiModel($this->db);
	
		//process request
		$mode = $model->mode($gameId);
	
		//return
		echo $mode;
	}
	
	//return game grid array
	function grid(){
		//grab input
		if(isset($_REQUEST['gameid'])){
			$gameId = $_REQUEST['gameid'];
		}
		else {
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_MISSING_GAMEID);
		}
	
		//get model
		require MODELS_PATH . 'ApiModel.php';
		$model = new ApiModel($this->db);
	
		//process request
		$data = $model->grid($gameId);
	
		//return
		header('Content-Type: application/json');
		echo $data;
	}
	
	//place move
	function move(){
		//grab input
		if(isset($_REQUEST['gameid'])){
			$gameId = $_REQUEST['gameid'];
		}
		else {
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_MISSING_GAMEID);
		}
		
		if(isset($_REQUEST['playerid'])){
			$playerId = $_REQUEST['playerid'];
		}
		else {
			die(MESSAGE_MISSING_PLAYERID);
		}
		
		if(isset($_REQUEST['position'])){
			$position = $_REQUEST['position'];
		}
		else {
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_INVALID_COMMAND);
		}
	
		//get model
		require MODELS_PATH . 'ApiModel.php';
		$model = new ApiModel($this->db);
	
		//process request
		$res = $model->move($gameId, $playerId, $position);
	
		//return
		header("HTTP/1.1 200 OK");
		echo $res;
	}
}
