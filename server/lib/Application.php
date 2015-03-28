<?php 
	//this prevents someone from bypassing the index.php file and going directly here
	if(!defined('_EXEC'))
	{
		die('Direct access not premitted');
	}

/**
 * This is the most important class, analyze the url and call controller methods
 */
class Application
{
    public function __construct()
    {
    	//define controller
    	$this->controller = null;
    	if(isset($_REQUEST['controller'])) {
    		$controllerName = ucfirst($_REQUEST['controller']);
    		$controllerPath = CONTROLLER_PATH . $controllerName . 'Controller.php';
    		if(file_exists($controllerPath)){
    			require $controllerPath;
    			$this->controller = new $controllerName();
    		}
    		else {
    			require CONTROLLER_PATH . 'IndexController.php';
    			$this->controller = new Index();
    		}
    	}
    	else {
    		require CONTROLLER_PATH . 'IndexController.php';
    		$this->controller = new Index();
    	}
    	
    	
    	//call method
    	if(isset($_REQUEST['method'])) {
    		$methodName = $_REQUEST['method'];
    		if(method_exists($this->controller, $methodName)){
    			$this->controller->{$methodName}();
    		}
    		else {
    			$this->controller->index();
    		}
    	}
    	else {
    		$this->controller->index();
    	}
    	
    }
}
