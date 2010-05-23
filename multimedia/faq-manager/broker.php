<?php
function conectar_bd ()
{
	include 'config.php';
	$id = mysql_connect($server, $login, $password)
			OR die ("Imposible conectar al servidor MySQL");
	$descriptor = mysql_select_db ($database, $id)
					OR die ("Imposible abrir el esquema de la base de datos");
	return $id;
}

function ejecutar_consulta($sql, $id)
{
	$result = mysql_query ($sql, $id)
			OR die ("Error al realizar la consulta: " . mysql_error()
			AND exit);
	return $result;
}

function cerrar_bd ($id)
{
	mysql_close ($id);
}



/*
include 'broker.php':
$id = conectar ();
$sql = "la consulta que queramos realizar";
$usuario_consulta = mysql_query ($sql, $id)
			OR die (Header ("Location: index.php?mod=error&error=1")
			AND exit);
			
-----------------------
Cerrar la conexión a la base de datos con:
mysql_close($id);
*/
?>