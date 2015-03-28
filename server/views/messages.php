<?php 
	//this prevents someone from bypassing the index.php file and going directly here
	if(!defined('_EXEC'))
	{
		die('Direct access not premitted');
	}
?>

<?php 
	if (isset($_SESSION['messages']) && count($_SESSION['messages']) > 0){
		foreach($_SESSION['messages'] as $message){
	
			echo "<b>".$message."<b></br>";
		}
		$_SESSION['messages'] = array();
		echo "<hr>";
	}
?>