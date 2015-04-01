<?php 
	//this prevents someone from bypassing the index.php file and going directly here
	if(!defined('_EXEC'))
	{
		die('Direct access not premitted');
	}
	
	/**
	 * This is the "base controller class". All other "real" controllers extend this class. 
	 * Whenever a controller is created, we also
	 * 1. initialize a session
	 * 2. check if the user is not logged in anymore (session timeout) but has a cookie
	 * 3. create a database connection (that will be passed to all models that need a database connection)
	 * 4. create a view object
	 */
	class Controller
	{
	    function __construct()
	    {
	        // if no session exist, start the session
	        if (session_id() == '') {
	        	session_start();
	        }
	
	        
	        // create database connection
	        if(DB_TYPE == "oracle"){
	        	$this->db = oci_connect ( DB_USER, DB_PASS, "gqian:1521/orcl" );
	        	if ($this->db == false) {
	        		$e = oci_error ();
	        		die($e);
	        		//TODO: add error page
	        	}
	        }
	        else{
	        	//mysql
	        	$options = array(PDO::ATTR_ERRMODE => PDO::ERRMODE_WARNING);
	        	$this->db = new PDO(DB_TYPE . ':host=' . DB_HOST . ';dbname=' . DB_NAME . ';charset=utf8', DB_USER, DB_PASS, $options);
	        }
	        
	       
	        // create a view object (that does nothing, but provides the view render() method)
	        $this->view = new View();
	    }
	
	    /**
	     * loads the model with the given name.
	     * @param $name string name of the model
	     */
	    public function loadModel($name)
	    {
	        $path = MODELS_PATH . strtolower($name) . '_model.php';
	
	        if (file_exists($path)) 
	        {
	            require_once $path;
	            // The "Model" has a capital letter as this is the second part of the model class name,
	            // all models have names like "LoginModel"
	            $modelName = $name . 'Model';
	            // return the new model object while passing the database connection to the model
	            return new $modelName($this->db);
	        }
	    }
	    
	   
	    
	    
	}
