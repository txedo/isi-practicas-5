# Introducción a Subversion #

A continuación explico cómo trabajar con subversion. Subversion es una herramienta muy útil para crear/usar repositorios de desarrollo. Aunque tiene un montón de opciones nosotros sólo usaremos dos o tres de ellas así que no os preocupéis.

## Cómo usar un repositorio ##

Hay unas reglas sencillas generales sobre los repositorios de desarrollo, tenedlas en cuenta a la hora de añadir nuevos ficheros al repositorio:

  * **NUNCA** se deben subir archivos generados automáticamente. A un repositorio **sólo** se suben archivos que hayamos creado nosotros o que no se puedan generar. Por ejemplo: como resultado de compilar un archivo latex podemos obtener un PDF, entonces subiremos el archivo latex pero **nunca** el PDF.
  * Hay que evitar subir archivos binarios, excepto aquellos que no queden más remedio, por ejemplo imágenes o sonidos creados por nosotros.
  * Cada vez que empecemos a trabajar con archivos del repositorio, comprobaremos primero que estemos en la última versión disponible.
  * Cuando terminemos trabajar, subiremos los cambios realizados.

## Cómo trabajar con subversion ##

Para trabajar con subversion primero debemos descargarnos una copia del repositorio. Esa copia se almacenará localmente, de esta forma podremos trabajar aunque no tengamos conexión a internet. Cuando descargamos una copia del repositorio tenemos que autenticarnos con el usuario y la contraseña que _googlecode_ nos asigna, pero esto sólo lo haremos la primera vez, en lo sucesivo los cambios se actualizarán y podremos subirlos sin necesidad de autenticarnos. Para descargar una copia de trabajo del repositorio ejecutamos lo siguiente:
```
$ svn checkout https://pgsi-g5.googlecode.com/svn/trunk/ pgsi-g5 --username usuario
```
Se pedirá entonces la contraseña generada en _googlecode_, una vez introducida se creará el directorio _pgsi-g5_ y en su interior estarán todos los ficheros del repositorio en su última versión. Podéis encontrar más información en http://code.google.com/p/pgsi-g5/source/checkout.

### Actualizar una copia local ###
Como ya dijimos anteriormente, antes de comenzar a trabajar tendremos que comprobar que no haya cambios nuevos en el repositorio, esto no es necesario si acabamos de bajarnos una copia local. Realmente no es obligatorio, pero nos evitará muchos problemas, así que, antes de comenzar a editar ningún fichero, actualizamos:
```
$ cd pgsi-g5
$ svn update
```
Esto actualizará toda la copia local completa. Si sólo quisiésemos actualizar un módulo (podemos decir que un módulo es un subdirectorio del repositorio), entraríamos en el directorio del módulo y ejecutaríamos el `svn update`.

### Subir cambios al repositorio ###
Cuando ya hemos finalizado nuestro trabajo o hemos terminado los cambios deseados, etc. tenemos que subir los cambios, eso lo hacemos con el siguiente comando:
```
$ svn ci
```
Este comando subirá al repositorio todos los ficheros distintos entre la copia local y la copia actual del repositorio, pero antes de subirlos, nos aparecerá un archivo de texto con los ficheros que sufrieron modificaciones. En ese archivo es conveniente anotar qué se hizo para así poder conocer por todos qué se va haciendo. Ya que en el _commit_ se anotan los ficheros modificados y quién los ha modificado, no es necesario anotar cosas como "modificacion de main.tex por tobias". Son más útiles anotaciones del tipo: "añadidas las nuevas secciones al archivo principal".

### Añadir un módulo al repositorio ###
Para crear un módulo (directorio) sólo hay que ejecutar:
```
$ svn mkdir nombre_modulo
```

Esto crea el directorio **pero no sube los cambios al repositorio**, para ello hay que ejecutar `svn ci`.

### Añadir un fichero/directorio al repositorio ###
Añadir ficheros/directorios (aseguraos que el directorio no contiene _basura_ antes de subirlo_) es también bastante sencillo, se usa el comando `add`, por ejemplo:
```
$ svn add main.tex
$ svn add *.tex
$ svn add doc/
```
Todo ello son ejemplos de cómo añadir ficheros, pero tened en cuenta que `add` **no sube** los ficheros, para terminar de subirlos, ya sabéis: `svn ci`._

Y esto es todo, cuando os familiaricéis con esto del `svn` ya os comentaré como mover/renombrar ficheros y como borrarlos. También hay un montón de opciones chulas como estadísticas y tal que nos puede venir bien para ver quién hace qué...

## Autor ##
Tobías Díaz Chirón