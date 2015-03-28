<?php 
	//this prevents someone from bypassing the index.php file and going directly here
	if(!defined('_EXEC'))
	{
		die('Direct access not premitted');
	}

	/**
	 * Class View
	 *
	 * Provides the methods all views will have
	 */
	class View
	{
	
		function __construct()
		{
			//nothing to do here
		}
	
		//display all the parts that make up the page
	    public function render($filename)
	    {
	    	require VIEWS_PATH . 'meta_head.php';
	    	require VIEWS_PATH . 'header.php';
	    	require VIEWS_PATH . 'menu.php';
	    	require VIEWS_PATH . 'messages.php';
	    	require VIEWS_PATH . $filename . '.php';
	    	require VIEWS_PATH . 'meta_foot.php';
	    }
	}
