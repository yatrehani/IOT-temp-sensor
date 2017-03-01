/**
 *
 * @author Yatin Rehani
 * Date 10-28-2016
 * This file implements the client which sends temperature readings from the sensors to the service
 * It simulates a sensor that sends messages to a service whenever measured
 * temperatures become too hot or too cold. These messages are sent using HTTP. The sensor messages are signed by the client.
 */
package project3task1client;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yatin Rehani
 * Date 10-28-2016
 * This class implements the client which sends temperature readings from the sensors to the service
 * It simulates a sensor that sends messages to a service whenever measured
 * temperatures become too hot or too cold. These messages are sent using HTTP. The sensor messages are signed by the client.
 */
public class Project3Task1Client {
    
BigInteger e1 = new BigInteger("65537");

BigInteger d1 = new BigInteger("339177647280468990599683753475404338964037287357290649639740920420195763493261892674937712727426153831055473238029100340967145378283022484846784794546119352371446685199413453480215164979267671668216248690393620864946715883011485526549108913");

BigInteger n1 = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");

BigInteger e2 = new BigInteger("65537");

BigInteger d2 = new BigInteger("3056791181023637973993616177812006199813736824485077865613630525735894915491742310306893873634385114173311225263612601468357849028784296549037885481727436873247487416385339479139844441975358720061511138956514526329810536684170025186041253009");

BigInteger n2 = new BigInteger("3377327302978002291107433340277921174658072226617639935915850494211665206881371542569295544217959391533224838918040006450951267452102275224765075567534720584260948941230043473303755275736138134129921285428767162606432396231528764021925639519");


    /**
     * 
     * This is the main method which creates new object of Project3Task1Client and calls other methods like highTemperature, lowTemperature, etc for sensor temperature 
     */
    public static void main(String[] args) {
        Project3Task1Client ptc3 = new Project3Task1Client();
        
        System.out.println(highTemperature("1", new Date().toString(), "Celsius", "30.6", ptc3.encryptMessage("1"+new Date().toString()+"Celsius"+"30.6", ptc3.d1, ptc3.n1)));
        System.out.println(lowTemperature("2", new Date().toString(), "Celsius", "0.7", ptc3.encryptMessage("2"+new Date().toString()+"Celsius"+"0.7", ptc3.d2, ptc3.n2)));
        System.out.println(highTemperature("1", new Date().toString(), "Celsius", "36.2", ptc3.encryptMessage("1"+new Date().toString()+"Celsius"+"36.2", ptc3.d1, ptc3.n1)));
        System.out.println(highTemperature("1", new Date().toString(), "Celsius", "30.0", ptc3.encryptMessage("1"+new Date().toString()+"Celsius"+"30.0", ptc3.d2, ptc3.n2)));
        System.out.println(getTemperatures());
        System.out.println(getLastTemperature("1"));
    }
    
    //Call to the server side, using JAX-WS to record a High Temperature reading
   private static String highTemperature(java.lang.String sensorId, java.lang.String timeStamp, java.lang.String type, java.lang.String temperature, java.lang.String signature) {
        sensorpackage.SensorService_Service service = new sensorpackage.SensorService_Service();
        sensorpackage.SensorService port = service.getSensorServicePort();
        return port.highTemperature(sensorId, timeStamp, type, temperature, signature);
    }

    //Call to the server side, using JAX-WS to record a Low Temperature reading
      private static String lowTemperature(java.lang.String sensorId, java.lang.String timeStamp, java.lang.String type, java.lang.String temperature, java.lang.String signature) {
        sensorpackage.SensorService_Service service = new sensorpackage.SensorService_Service();
        sensorpackage.SensorService port = service.getSensorServicePort();
        return port.lowTemperature(sensorId, timeStamp, type, temperature, signature);
    }
    //Call to the server side, using JAX-WS to get all recorded temperatures
    private static String getTemperatures() {
        sensorpackage.SensorService_Service service = new sensorpackage.SensorService_Service();
        sensorpackage.SensorService port = service.getSensorServicePort();
        return port.getTemperatures();
    }
    //Call to the server side, using JAX-WS to get last recorded temperature of a given sensor
   
    private static String getLastTemperature(java.lang.String sensorId) {
        sensorpackage.SensorService_Service service = new sensorpackage.SensorService_Service();
        sensorpackage.SensorService port = service.getSensorServicePort();
        return port.getLastTemperature(sensorId);
    }
    //Encrypt the source String to be sent to the server
    private String encryptMessage(String inputString, BigInteger d, BigInteger N){
        byte[] digest = null;
        String encryptedMessage =null;
        try {
            //Get the bytes from the source string and compute a SHA-1 digest of these bytes.
            digest = MessageDigest.getInstance("SHA1").digest(inputString.getBytes());
            byte[] digestCopy = new byte [digest.length+1];
            //Copy these bytes into a byte array that is one byte longer than needed. The resulting byte array has its extra byte set to 1
            digestCopy[0] = 1;
            for(int i=0;i<digest.length;i++)
            {
                digestCopy[i+1] = digest[i];
            }
            String digestCopyString = javax.xml.bind.DatatypeConverter.printHexBinary(digestCopy);
            // Create a BigInteger from the above String. Encrypt this BigInteger with RSA d and n. Return to the caller a String representation of this BigInteger
            encryptedMessage = ((new BigInteger(digestCopyString.getBytes())).modPow(d, N)).toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Project3Task1Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encryptedMessage;
    }
}

    

    

  

    


   