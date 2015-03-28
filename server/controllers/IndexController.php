<?php 
	//this prevents someone from bypassing the index.php file and going directly here
	if(!defined('_EXEC'))
	{
		die('Direct access not premitted');
	}

	class Index extends Controller
	{
		public function __construct()
		{
			parent::__construct();
			
		}
		
		//display home page
		function index(){
			$this->view->render('index/index');
		}
		
	}




