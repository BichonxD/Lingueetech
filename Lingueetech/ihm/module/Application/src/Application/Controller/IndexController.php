<?php
/**
 * Zend Framework (http://framework.zend.com/)
 *
 * @link      http://github.com/zendframework/ZendSkeletonApplication for the canonical source repository
 * @copyright Copyright (c) 2005-2013 Zend Technologies USA Inc. (http://www.zend.com)
 * @license   http://framework.zend.com/license/new-bsd New BSD License
 */

namespace Application\Controller;

use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;
use Zend\Json\Json;
use Application\Service\SocketManager;

class IndexController extends AbstractActionController
{
    public function indexAction()
    {
        // Envoyer un message auserveurs
        $str = '{"request"   : { "action"    : "Search","params"    : { "query"     : "Jambon" }}}';
        
        
        SocketManager::sendJsonMessage(  $str . "\n" ) ; 
        $json =   SocketManager::readResponse();
         
        // Retourner la reponse au client
        $response = Json::prettyPrint($json) ; 
        return new ViewModel(array('result' =>  $response));
    }
}
