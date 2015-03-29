# Introducción a LaTeX #

A continuación explicaremos brevemente cómo escribir un documento LaTeX. Los documentos tienen siempre la misma estructura: una cabecera que configura el documento y el contenido en sí. Nosotros estructuraremos el documento en varios ficheros, de forma que cada uno de nosotros sólo tendrá que modificar aquellos que tenga asignados. Un archivo principal contendrá la cabecera e incluirá los ficheros con el contenido del documento. Del archivo principal (y por tanto de cómo escribir cabeceras LaTeX) no contaremos nada, no es necesario, así que sólo hablaremos de cómo formatear el contenido de los documentos _tipo artículo_ en LaTeX, que será lo que nosotros escribiremos.

A la hora de escribir un documento en LaTeX no hay que preocuparse de los saltos de línea ni nada por el estilo, nosotros simplemente escribiremos el contenido en un archivo de texto plano y LaTeX se encargará de formatearlo.

Una recomendación es usar **emacs** (con **auctex**) para editar ficheros de LaTeX, esto nos hará más fácil la vida a la larga, ya que LaTeX usa símbolos especiales que **auctex** usa automáticamente y nos evita tener que usarlos nosotros. Existen también otros editores más amigables como **kyle**, aunque personalmente nunca los he usado. **Auctex** tiene además una gran cantidad de teclas rápidas que nos facilitarán la edición, estas secuencias de teclas rápidas indicarán también.

## Secciones ##
Un artículo se dividirá en secciones, subsecciones y sub-subsecciones, **no existe más nivel de anidamiento**. Para facilitarnos las cosas cada archivo corresponderá con una sección completa. En LaTeX las secciones se inician de la siguiente manera:
```
\section{Nombre sección}
Esto es una sección.
\subsection{Nombre subsección}
Esto es una subsección de la sección.
\subsection{Nombre de otra subsección}
Esto es otra subsección de la sección.
\subsubsection{Nombre subsubsección}
Esto es una subsubsección de la segunda subsección.
```

Esto se traduciría más o menos así:
# 1.Nombre sección #
Esto es una sección.
## 1.1.Nombre subsección ##
Esto es una subsección de la sección.
## 1.2.Nombre de otra subsección ##
Esto es otra subsección de la sección.
### 1.2.1.Nombre subsubsección ###
Esto es una subsubsección de la segunda subsección.

Ahora puede parecer un poco lioso... pero se ve más claro en un documento normal.
Para crear secciones y subsecciones automáticamente en **auctex** usaremos la combinación `Ctrl-C Ctrl-S`. Todas las combinaciones de teclas en **auctex** comienzan siempre por `Ctrl-C`.

## Entornos ##
Los entornos son partes del documento _especiales_, como puede ser una figura (imagen), una lista de elementos, una enumeración, etc. En **auctex** se crean con `Ctrl-C Ctrl-E`, se nos pedirá qué tipo de entorno queremos crear y los datos necesarios para crearlo.

### Lista de elementos ###
Las listas de elementos son estructuras del tipo:
  * Elemento
  * Elemento
  * ...

En LaTeX se escribe:
```
\begin{itemize}
\item Elemento
\item Elemento
\item
  \begin{itemize}
  \item Subelemento
  \item Subelemento
  \end{itemize}
\end{itemize}
```
Y eso se renderizaría en:
  * Elemento
  * Elemento
    * Subelemento
    * Subelemento

Si usamos **auctex** no es necesario estar escribiendo `\item` cada vez, bastará con pulsar `ESC INTRO` para cada nueva línea.

### Enumeración de elementos ###
Las enumeracines son iguales que las listas, pero en vez de un guión, cada elemento se enumera, es decir:
  1. Elemento 1
  1. Elemento 2
  1. ...

En LaTeX se hace de la siguiente manera:
```
\begin{enumerate}[1]
\item Elemento 1
\item Elemento 2
\end{enumerate}
```
Esto quedaría como:
  1. Elemento 1
  1. Elemento 2

Pero podríamos hacer enumerados con otro tipo de cuenta, por ejemplo:
```
\begin{enumerate}[i]
\item Elemento 1
\item Elemento 2
\end{enumerate}
```
Que se vería tal que (no lo puedo mostrar bien en el wiki):
```
i. Elemento 1
ii.Elemento 2
```

Son válidos modificadores como `[a]`, `[i]`, `[I]` o `[1]`. También podemos empezar la cuenta desde otro número, por ejemplo `[iv]`.

### Insertar imágenes ###
Para insertar una imagen desde un archivo escribiremos lo siguiente:
```
\begin{figure}[!h]
  \centering
  \includegraphics[scale=0.30]{archivo.png}
  \caption{Título de la imagen}
\end{figure}
```

Esto inserta una imagen centrada, con la escala determinada. Se puede eliminar el factor de escala y especificar directamente un ancho con `[width=10cm]` (por ejemplo). Al pie de la imagen se indicará el título que se haya especificado. Un comando útil es `\label{nombre_etiqueta}` que creará un _hiperenlace_ interno. Esto se explicará (si es necesario) más adelante.

## Tipos de letra básicos ##
Los tipos básicos son **bold**, _emphatized_ y `teletype`, que en LaTeX se escribiría así:
```
Esto es \textbf{texto en negrita}, también podemos usar \emph{texto enfatizado}.
Otro tipo más raro es el \texttt{texto teletipo}.
```

Esto nos daría algo parecido a:

Esto es **texto en negrita**, también podemos usar _texto enfatizado_.
Otro tipo más raro es el `texto teletipo`.

En **auctex** las teclas para introducir estos comandos son `Ctrl-C Ctrl-F Ctrl-B` para **bold**, `Ctrl-C Ctrl-F Ctrl-E` para _emphatized_ y `Ctrl-C Ctrl-F Ctrl-T` para `teletype`.


Y con esto podemos escribir la mayoría de los elementos que usamos en nuestros documentos. Otra cosa sería tener que utilizar tablas o elementos más complejos, que si hacen falta, se explican y tan contentos...

## Autor ##
Tobías Díaz Chirón