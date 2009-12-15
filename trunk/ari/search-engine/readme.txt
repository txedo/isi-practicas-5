Software necesario que debe ser instalado en el sistema sobre el que
se ejecutará la aplicación:
· python2.5 o superior
· gtk
· pygtk
· mysql
· python-mysqldb
· psyco

Inicialmente se debe crear la base de datos que utilizará el sistema
documental. En la distribución del software se ofrece un fichero
"install" bajo el directorio raíz, que contiene las
sentencias necesarias para crear la base de datos y sus tablas
mediante el intérprete de MySQL. Para acceder al intérprete de
su servidor MySQL escriba la siguiente sentencia en un terminal:
mysql -u root -p

Llegados a este punto ya tiene su sistema listo para ser utilizado. Si
tiene alguna duda sobre su uso consulte el Manual de Usuario
suministrado con la documentación del sistema.


pyDMS v1.0 (Search Engine + Index Engine)
-----------------------------------------
./searchEngineGUI.py


Index Engine (standalone)
-------------------------
Interfaz gráfica de usuario
---------------------------
./indexEngineGUI.py

Interfaz basada en texto
------------------------
./indexEngineCMD.py ([-f | --file] <file_path> | [-d | --directory]
<directory_path>)


