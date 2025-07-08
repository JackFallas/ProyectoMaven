package org.jackfallas.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Eduardo
 */
public class Conexion {
    //Instancia Unica a la clase Conexion
    //Esta variable almacenará la única instancia de esta clase que existirá en la aplicación.
    private static Conexion instancia;
    //Se declara una variable de instancia privada llamada `conexion` del tipo `java.sql.Connection`.
    //Declaracion de la variable para la conexion de la base de Datos, Esta variable representará  la conexión activa con la base de datos MySQL.
    private Connection conexion;
    //Definición de la URL de conexión a la base de datos MySQL que contiene la cadena de conexión JDBC (Java Database Connectivity) para MySQL.
    //- `jdbc:mysql://`: Indica el subprotocolo JDBC para MySQL.
    //      - `127.0.0.1`: Es la dirección IP local (localhost), donde se supone que está
    //        ejecutándose el servidor de MySQL.
    //      - `3306`: Es el puerto predeterminado en el que MySQL escucha las conexiones.
    //      - `/b2veterniariadb`: Es el nombre de la base de datos a la que se desea conectar.
    //      - `?useSSL=false`: Es un parámetro adicional en la URL que desactiva el uso
    //        de Secure Sockets Layer (SSL) para la conexión. Esto se suele hacer en entornos
    //        de desarrollo local para simplificar la configuración. En producción,
    //        generalmente se recomienda usar SSL para una conexión segura.
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/HyprLandDB?useSSL=false";
    //User y password almacenan el nombre de usuario y la contraseña de donde se tiene la base de datos en Workbench para lograr la conexion al servidor de MySql
    private static final String user = "quintom";
    private static final String password = "admin";
    //Definición del nombre de la clase del driver JDBC de MySQL
    //      Se define una constante llamada `driver` que contiene el nombre completo de la clase del
    //      driver JDBC proporcionado por MySQL. Este driver es un conjunto de archivos
    //      (normalmente un archivo JAR) que permite a Java comunicarse con la base de datos
    //      MySQL utilizando el protocolo JDBC.
    private static final String driver = "com.mysql.jdbc.Driver";
    
    // Constructor de la clase Conexion en donde se llama el metodo conectar() para intentar lograr la conexion inmediatamente
    public void Conexion(){
        conectar();
    }
    
    // 8. Método para establecer la conexión a la base de datos
    //    Este método contiene la lógica para crear la conexión con la base de datos MySQL.
    public void conectar(){
        try { 
            // a. Carga dinámicamente la clase del driver JDBC en memoria
            //intenta cargar en memoria la clase especificada por la variable `driver` ("com.mysql.jdbc.Driver"). Esto registra el
            //driver JDBC con el `DriverManager`. `.newInstance()` intenta crear una nueva instancia de esta clase. El propósito es
            //asegurar que el driver esté disponible para crear conexiones.
            Class.forName(driver).newInstance();  
            //Establece la conexión utilizando el DriverManager _conexion = DriverManager.getConnection(URL, user, password);_:
            // `DriverManager.getConnection()` es un método estático de la clase `DriverManager` que intenta establecer una conexión con la base de datos
            //especificada por la `URL`, utilizando el `user` y la `password` proporcionados. Si la conexión es exitosa, devuelve un objeto de tipo `Connection` que se
            //asigna a la variable de instancia `conexion`.
            conexion = DriverManager.getConnection(URL, user, password);
            System.out.println("Conexion exitosa");
            //- `ClassNotFoundException`: Se lanza si la clase del driver especificada
            //        en `driver` no se puede encontrar en el classpath de la aplicación.
            //      - `InstantiationException`: Se lanza si no se puede crear una instancia
            //        de la clase del driver (por ejemplo, si la clase es abstracta).
            //      - `IllegalAccessException`: Se lanza si la aplicación no tiene permiso
            //        para acceder al constructor de la clase del driver.
            //      - `SQLException`: Es una excepción general que cubre cualquier error
            //        relacionado con la base de datos, como una URL incorrecta,
            //        credenciales inválidas o problemas con el servidor de MySQL.
        }catch (ClassNotFoundException|InstantiationException|
                IllegalAccessException|SQLException ex) {
            System.out.println("Error al conectar");
            // e. Imprime la traza de la excepción para obtener más detalles del error
            //    _ex.printStackTrace();_: Imprime en la consola la pila de llamadas que
            //      llevaron a la excepción. Esto es muy útil para diagnosticar la causa
            //      del error durante el desarrollo.
            ex.printStackTrace();
        }

    }
    // 9. Método estático para obtener la instancia única de la clase Conexion Este método proporciona un punto
    // de acceso global para obtener la única instancia de la clase `Conexion`.
    public static Conexion getInstancia() {
         //Verifica si la instancia ya ha sido creada _if ( instancia == null) { ... }_: Se comprueba si la variable estática
        // `instancia` es nula. Esto significa que aún no se ha creado ninguna instancia de la clase `Conexion`.
        if ( instancia == null) {
            // b. Si no existe, crea una nueva instancia de Conexion Si `instancia` es nula, se crea una nueva
            // instancia de la clase `Conexion` utilizando el constructor. Esta instancia
            // se guarda en la variable estática `instancia`. La próxima vez que se llame a `getInstancia()`, esta instancia ya existirá y se devolverá.
            instancia = new Conexion();
        }
         // c. Retorna la instancia única de la clase Conexion Devuelve la referencia a la única instancia de la clase
        // `Conexion`. Esto asegura que toda la aplicación utilice la misma conexión a la base de datos
        return instancia;
    }
    
    // 10. Método público para obtener la conexión a la base de datos Este método permite a otras partes
    // de la aplicación obtener el objeto `Connection` para interactuar con la base de datos.
     public Connection getConexion() {
        try {
            // a. Verifica si la conexión es nula o si está cerrada Antes de devolver
            //la conexión, se verifica si el objeto `conexion` es nulo (nunca se ha
            //establecido una conexión) o si la conexión actual está cerrada.
            if (conexion == null || conexion.isClosed()) {
                 // b. Si la conexión no existe o está cerrada, intenta establecer una nueva conexión Si la conexión no está activa, se llama al método `conectar()`
                // para intentar establecer una nueva conexión con la base de datos. Esto asegura que siempre se devuelva una conexión válida (si es posible).
                conectar();
            }
        } catch (SQLException eX) {
            eX.printStackTrace();
        }
        // d. Retorna la instancia de la conexión Devuelve el objeto `Connection` actual. Si no se pudo
        //establecer una conexión, este objeto podría ser nulo (aunque el método `conectar()` intentará establecerla si es necesario). Es importante manejar posibles
        //conexiones nulas en el código que utiliza este método.
        return conexion;
    }
}

