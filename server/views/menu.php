<?php 
	//this prevents someone from bypassing the index.php file and going directly here
	if(!defined('_EXEC'))
	{
		die('Direct access not premitted');
	}
?>

	<a href="<?php echo URL?>?controller=index&method=index">Home</a> |
	<a href="<?php echo URL?>?controller=api&method=index">Protocol</a> |
	<a href="http://cs2.uco.edu/~gq011/tictactoe/client/" target="_blank" >Client</a> |

<hr>