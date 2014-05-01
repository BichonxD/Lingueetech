<?php
namespace Application\Service;

class SocketManager {
    
    const PORT = 1603;
    
    const IP_ADRESS = '127.0.0.1';
    
    /**
     * Ressource de connexion a l'application Java
     * Doit etre crŽer par l'appel a socket_create
     * 
     * @var ressource
     */
    public static  $sock;
    
    /**
     * 
     * 
     * @var ressource
     */
    public static $connexionId;
    
    public  static function initialize(){
        
        // CrŽation d'un nouveau socket
        self::$sock = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
         
        // Lie l'adresse de la source
        socket_bind( self::$sock ,  self::IP_ADRESS);
        
        // Connexion ˆ l'adresse de destination
        socket_connect(self::$sock , self::IP_ADRESS, self::PORT);
                         
    }
    
    public static function sendJsonMessage($jsonMessage){
        socket_write( self::$sock , $jsonMessage);
    }
    
    public static function readResponse(){
       $response =  socket_read(self::$sock, 1024) ;
       return $response;
    }
    
    public static function closeConnexion(){
               
        // Fermeture
        socket_close( self::$sock );
    }
}