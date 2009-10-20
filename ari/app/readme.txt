Software necesario que debe ser instalado en el sistema sobre el que
se ejecutará la aplicación:
· python2.5 o superior
· gtk
· pygtk
· mysql
· python-mysqldb

Ahora debe crear la base de datos que utilizará el sistema
documental. En la distribución del software se ofrece un fichero
``install'' bajo el directorio ``persistencia'', que contiene las
sentencias necesarias para crear la base de datos y sus tablas
mediante el intérprete de MySQL. Si no sabe acceder al intérprete de
su servdor MySQL escriba la siguiente sentencia en un terminal:
\texttt{mysql -u root -p}

Llegados a este punto ya tiene su sistema listo para ser utilizado. Si
tiene alguna duda sobre su uso consulte el Manual de Usuario
suministrado con la documentación del sistema.


Interfaz gráfica de usuario
---------------------------
./index-engine-gui.py


Interfaz basada en texto
------------------------
./index-engine-cmd.py ([-f | --file] <file_path> | [-d | --directory]
<directory_path>)

