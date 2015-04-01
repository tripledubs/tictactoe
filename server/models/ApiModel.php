<?php 
	//this prevents someone from bypassing the index.php file and going directly here
	if(!defined('_EXEC'))
	{
		die('Direct access not premitted');
	}

	
class ApiModel {

	public function __construct($db)
	{
		$this->db = $db;
	}
	public function start($boardSize){

		//create random id
		$gameId = file_get_contents('/dev/urandom', false, null, 0, 2);
		$gameId = bin2hex($gameId);
		
		$gamedata = Array("","","","","","","","","");
		
		//insert into database
		$sql = "Insert INTO games (gid, status, gamemode, gamedata) VALUES (:gid, 0, 'tictactoe', :gamedata)";
		if(DB_TYPE == "oracle"){
			$cursor = oci_parse($this->db, $sql);
			oci_bind_by_name($cursor, ':gid', $gameId, -1);
			oci_bind_by_name($cursor, ':gamedata', json_encode($gamedata, true), -1);
			$result = oci_execute($cursor);
			
			//check for error
			if ($result == false){
				$e = oci_error($cursor);
				header("HTTP/1.1 400 Bad Request");
				die(MESSAGE_DATABASE_ERROR);
			}
		}
		else{
			try{
				$stmt = $this->db->prepare($sql);
				$stmt->bindParam(':gid', $gameId, PDO::PARAM_STR);
				$stmt->bindParam(':gamedata', json_encode($gamedata, true), PDO::PARAM_STR);
				$stmt->execute();
				//$result = $stmt->fetchAll();
			}
			catch(PDOException $e)
			{
				//$errar = $e->errorInfo[2];
				die(MESSAGE_DATABASE_ERROR);
			}
		}
		
		//return
		return $gameId;
	}
	
	public function connect($gameId){
	
		//create random id
		$playerId = file_get_contents('/dev/urandom', false, null, 0, 16);
		$playerId = bin2hex($playerId);
		
		//grab the game
		$game = $this->getGame($gameId);
		if($game == false){
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_GAME_NOT_FOUND);
		}
		
		if( empty($game['P1ID']) ){
			//add p1
			$sql = "UPDATE games SET p1id=:pid WHERE gid=:gid";
		}
		else if( empty($game['P2ID']) ){
			//add p2
			$sql = "UPDATE games SET p2id=:pid, status=1 WHERE gid=:gid";
		}
		else {
			//game is full
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_GAME_FULL);
		}
		
		//update db
		if(DB_TYPE == "oracle"){
			$cursor = oci_parse($this->db, $sql);
			oci_bind_by_name($cursor, ':gid', $gameId, -1);
			oci_bind_by_name($cursor, ':pid', $playerId, -1);
			$result = oci_execute($cursor);
		
			//check for error
			if ($result == false){
				$e = oci_error($cursor);
				header("HTTP/1.1 400 Bad Request");
				die(MESSAGE_DATABASE_ERROR);
			}
		}
		else{
			try{
				$stmt = $this->db->prepare($sql);
				$stmt->bindParam(':gid', $gameId, PDO::PARAM_STR);
				$stmt->bindParam(':pid', $playerId, PDO::PARAM_STR);
				$stmt->execute();
				//$result = $stmt->fetchAll();
			}
			catch(PDOException $e)
			{
				//$errar = $e->errorInfo[2];
				die(MESSAGE_DATABASE_ERROR);
			}
		}
		
		
		//return
		return $playerId;
	}
	
	public function status($gameId){
	
		//grab the game
		$game = $this->getGame($gameId);
		if($game == false){
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_GAME_NOT_FOUND);
		}
	
		//return
		return $game['STATUS'];
	}
	
	public function mode($gameId){
	
		//grab the game
		$game = $this->getGame($gameId);
		if($game == false){
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_GAME_NOT_FOUND);
		}
	
		//return
		return $game['GAMEMODE'];
	}
	
	public function grid($gameId){
	
		//grab the game
		$game = $this->getGame($gameId);
		if($game == false){
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_GAME_NOT_FOUND);
		}
	
		//return
		return $game['GAMEDATA'];
	}
	public function move($gameId, $playerId, $position){
		//define valid moves for slide mode
		$validMoves = array();
		$validMoves[] = array(1,3,4);
		$validMoves[] = array(0,2,4);
		$validMoves[] = array(1,5,4);
		$validMoves[] = array(0,4,6);
		$validMoves[] = array(0,1,2,3,4,5,6,7);
		$validMoves[] = array(2,4,8);
		$validMoves[] = array(3,7,4);
		$validMoves[] = array(4,6,8);
		$validMoves[] = array(5,7,4);
		
		
		//grab the game
		$game = $this->getGame($gameId);
		if($game == false){
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_GAME_NOT_FOUND);
		}
		
		//cant move if you're not part of this game
		if($game['P1ID'] == $playerId){
			$pvalue = "X";
			$pnum = 1;
		}
		else if($game['P2ID'] == $playerId){
			$pvalue = "O";
			$pnum = 2;
		}
		else {
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_PLAYER_NOT_IN_GAME);
		}
		
		//must be your turn
		if($game['STATUS'] == 0){
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_GAME_NOT_STARTED);
		}
		else if($game['STATUS'] == 1 && $pnum != 1){
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_NOT_YOUR_TURN);
		}
		else if($game['STATUS'] == 2 && $pnum != 2){
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_NOT_YOUR_TURN);
		}
		else if($game['STATUS'] == 3 || $game['STATUS'] == 4){
			header("HTTP/1.1 400 Bad Request");
			die(MESSAGE_GAME_OVER);
		}
		
		//parse the game board
		$grid = json_decode($game['GAMEDATA']);
		
		$result = false;
		if($game['GAMEMODE'] == "slide"){
			//cant move for another player
			if($grid[$position] != $pvalue ){
				header("HTTP/1.1 400 Bad Request");
				die(MESSAGE_INVALID_COMMAND);
			}
			
			//the square to move must be yours
			if($grid[$position] != $pvalue ){
				header("HTTP/1.1 400 Bad Request");
				die(MESSAGE_INVALID_COMMAND);
			}
			
			//find empty square
			foreach($grid as $key=>$val){
				if($val == ""){
					$emptySquare = $key;
				}
			}
			
			//the square to move must be next to the empty square
			if( in_array($position, $validMoves[$emptySquare]) ){
				
				//update grid
				$grid[$emptySquare] = $grid[$position];
				$grid[$position] = "";
				
				$result = true;
			}
			else {
				header("HTTP/1.1 400 Bad Request");
				die(MESSAGE_INVALID_COMMAND);
			}
		}
		else {
			
			if($grid[$emptySquare] != "" ){
				header("HTTP/1.1 400 Bad Request");
				die(MESSAGE_INVALID_COMMAND);
			}
			
			//the move is valid
			$grid[$position] = $pvalue;
			
			$result = true;
		}
		
		//update mode
		$mode = "tictactoe";
		$count = 0;
		foreach($grid as $key=>$val){
			if($val != ""){
				$count++;
			}
		}
		if($count >= 8 ){
			$mode = "slide";
		}
		
		//update the game status
		$status = $this->checkWinner($grid);
		if($status == null){
				
			//toggle turn
			if($game['STATUS'] == 1){
				$status = 2;
			}
			else{
				$status = 1;
			}
				
			// in slide move if other player is traped you win
			if($mode == "slide"){
				//find empty square again
				foreach($grid as $key=>$val){
					if($val == ""){
						$emptySquare = $key;
					}
				}
				//check if any moves are left for the other player
				$movesLeft = false;
				foreach($validMoves[$emptySquare] as $move){
					if($grid[$move] != $pvalue){
						$movesLeft = true;
					}
				}
				if(!$movesLeft){
					//you win
					$status = 2+$pnum;
				}
				
			}
		}

		//update db
		$sql = "UPDATE games SET gameData=:gameData, status=:status, gameMode=:gameMode WHERE gid=:gid";
		if(DB_TYPE == "oracle"){
			$cursor = oci_parse($this->db, $sql);
			oci_bind_by_name($cursor, ':gid', $gameId, -1);
			oci_bind_by_name($cursor, ':gameData', json_encode($grid, true), -1);
			oci_bind_by_name($cursor, ':status', $status, -1);
			oci_bind_by_name($cursor, ':gameMode', $mode, -1);
			$result = oci_execute($cursor);
			
			//check for error
			if ($result == false){
				$e = oci_error($cursor);
				header("HTTP/1.1 400 Bad Request");
				die(MESSAGE_DATABASE_ERROR);
			}
		}
		else{
			try{
				$stmt = $this->db->prepare($sql);
				$stmt->bindParam(':gid', $gameId, PDO::PARAM_STR);
				$stmt->bindParam(':gameData', json_encode($grid, true), PDO::PARAM_STR);
				$stmt->bindParam(':status', $status, PDO::PARAM_STR);
				$stmt->bindParam(':gameMode', $mode, PDO::PARAM_STR);
				$stmt->execute();
				//$result = $stmt->fetchAll();
			}
			catch(PDOException $e)
			{
				//$errar = $e->errorInfo[2];
				die(MESSAGE_DATABASE_ERROR);
			}
		}
		
		
		return true;
		
	}

	private function checkWinner($grid){
		
		$winner = "";
		//012
		if($grid[0] != "" && $grid[0] == $grid[1] && $grid[0] == $grid[2]){
			$winner = $grid[0];
		}
		//345
		if($grid[3] != "" && $grid[3] == $grid[4] && $grid[3] == $grid[5]){
			$winner = $grid[3];
		}
		//678
		if($grid[6] != "" && $grid[6] == $grid[7] && $grid[6] == $grid[8]){
			$winner = $grid[6];
		}
		//036
		if($grid[0] != "" && $grid[0] == $grid[3] && $grid[0] == $grid[6]){
			$winner = $grid[0];
		}
		//147
		if($grid[1] != "" && $grid[1] == $grid[4] && $grid[1] == $grid[7]){
			$winner = $grid[1];
		}
		//258
		if($grid[2] != "" && $grid[2] == $grid[5] && $grid[2] == $grid[8]){
			$winner = $grid[2];
		}
		//048
		if($grid[0] != "" && $grid[0] == $grid[4] && $grid[0] == $grid[8]){
			$winner = $grid[0];
		}
		//246
		if($grid[2] != "" && $grid[2] == $grid[4] && $grid[2] == $grid[6]){
			$winner = $grid[2];
		}
		
		
		if($winner == "X"){
			return 3;
		}
		else if($winner == "O"){
			return 4;
		}
		else{
			return null;
		}
		
	}
	
	private function getGame($gameId){
		
		$sql = "SELECT * FROM games WHERE gid=:gid";
		
		
		if(DB_TYPE == "oracle"){
			$cursor = oci_parse($this->db, $sql);
			oci_bind_by_name($cursor, ':gid', $gameId, -1);
			$result = oci_execute($cursor);
			
			//check for error
			if ($result == false){
				$e = oci_error($cursor);
				header("HTTP/1.1 400 Bad Request");
				die(MESSAGE_DATABASE_ERROR);
			}
			
			$values = oci_fetch_array($cursor);
		}
		else{
			try{
				$stmt = $this->db->prepare($sql);
				$stmt->bindParam(':gid', $gameId, PDO::PARAM_STR);
				$stmt->bindParam(':gameData', json_encode($grid, true), PDO::PARAM_STR);
				$stmt->bindParam(':status', $status, PDO::PARAM_STR);
				$stmt->bindParam(':gameMode', $mode, PDO::PARAM_STR);
				$stmt->execute();
				$values = $stmt->fetchAll();
			}
			catch(PDOException $e)
			{
				//$errar = $e->errorInfo[2];
				die(MESSAGE_DATABASE_ERROR);
			}
		}
		
		

		return $values;
	}

}