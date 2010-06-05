<?php
// Comprobamos que esta definido el tipo de operacion (add, del, edit, view)
if (isset($_GET['op'])) {
	include 'broker.php';
	$operacion = $_GET['op'];
	if ($operacion == "add") {
		if (isset($_GET['pregunta']) && isset($_GET['respuesta']) && isset($_GET['categoria'])) {
			$id = conectar_bd();
			$categoria = $_GET['categoria'];
			$sql = "SELECT id FROM categoria_faq WHERE categoria='$categoria'";
			$res = ejecutar_consulta ($sql, $id);
			$fila = mysql_fetch_array ($res);
			$idCategoria = $fila["id"];
			$sql = "INSERT INTO faq (pregunta, respuesta, idCategoria) VALUES ('".$_GET['pregunta']."','".$_GET['respuesta']."',".$idCategoria.")";
			$res = ejecutar_consulta ($sql, $id);
			if ($res) {
				echo "&estado=ok";
				echo "&mensaje=La pregunta se ha creado con exito.";
			}
			else {
				echo "&estado=error";
				echo "&mensaje=No se ha podido crear la pregunta. " . mysql_error();
			}
			cerrar_bd ($id);
		}
		else {
			echo "&estado=error";
			echo "&mensaje=Debe especificar la pregunta, la respuesta y la categoria de la misma.";
		}
	}
	else if ($operacion == "view") {
		$id = conectar_bd();
		if (isset($_GET['pregunta'])) {
			$pregunta = $_GET['pregunta'];
			if ($pregunta!="") {
				// Dada una pregunta -> devolver respuesta y categoria
				$sql = "SELECT respuesta,categoria FROM faq f, categoria_faq c WHERE f.idCategoria=c.id AND f.pregunta='$pregunta'";
				$res = ejecutar_consulta ($sql, $id);
				$fila = mysql_fetch_array ($res);
				$respuesta = $fila["respuesta"];
				$categoria = $fila["categoria"];
				echo "&respuesta=$respuesta&categoria=$categoria";
				echo "&estado=ok";
			}
		}
		else if (isset($_GET['categoria'])) {
			$categoria = $_GET['categoria'];
			if ($categoria!="") {
				// Dada una categoria -> devolver todas sus preguntas y respuestas
				$sql = "SELECT pregunta,respuesta FROM faq f, categoria_faq c WHERE f.idCategoria=c.id AND c.categoria='$categoria' ORDER BY categoria";
				$res = ejecutar_consulta ($sql, $id);
				$contador = 0;
				while ($fila = mysql_fetch_array ($res)) {
					$pregunta = $fila["pregunta"];
					$respuesta = $fila["respuesta"];
					echo "&pregunta$contador=$pregunta&respuesta$contador=$respuesta";
					$contador++;
				}
				echo "&estado=ok";
				echo "&contador=$contador";
			}
		}
		else {
			// Si no se indica nada -> devolver todos los títulos
			$sql = "SELECT pregunta FROM faq";
			$res = ejecutar_consulta ($sql, $id);
			$contador = 0;
			while ($fila = mysql_fetch_array ($res)) {
				$pregunta = $fila["pregunta"];
				echo "&pregunta$contador=$pregunta";
				$contador++;
			}
			echo "&estado=ok";
			echo "&contador=$contador";
		}
		cerrar_bd ($id);
	}
	else if ($operacion == "modify") {
		$id = conectar_bd();
		if (isset($_GET['pregunta']) && isset($_GET['categoria']) && isset($_GET['respuesta'])) {
			$pregunta = $_GET['pregunta'];
			$categoria= $_GET['categoria'];
			$respuesta = $_GET['respuesta'];
			if ($pregunta!="") {
				$sql = "UPDATE faq SET idCategoria=(SELECT id FROM categoria_faq WHERE categoria='$categoria'), respuesta='$respuesta' WHERE pregunta='$pregunta'";
				$res = ejecutar_consulta ($sql, $id);
				if ($res) {
					echo "&estado=ok";
					echo "&mensaje=La pregunta ha sido modificada correctamente.";
				}
				else {
					echo "&estado=error";
					echo "&mensaje=No se ha podido modificar la pregunta. " . mysql_error();
				}
			}
		}
		else {
			echo "&estado=error";
			echo "&mensaje=Debe especificar la pregunta que desea eliminar junto con su categoría y respuesta.";
		}
		cerrar_bd ($id);
	}
	else if ($operacion == "del") {
		$id = conectar_bd();
		if (isset($_GET['pregunta'])) {
			$pregunta = $_GET['pregunta'];
			if ($pregunta!="") {
				$sql = "DELETE FROM faq WHERE pregunta='$pregunta'";
				$res = ejecutar_consulta ($sql, $id);
				if ($res) {
					echo "&estado=ok";
					echo "&mensaje=La pregunta ha sido eliminada.";
				}
				else {
					echo "&estado=error";
					echo "&mensaje=No se ha podido eliminar la pregunta. " . mysql_error();
				}
			}
		}
		else {
			echo "&estado=error";
			echo "&mensaje=Debe especificar la pregunta que desea eliminar.";
		}
		cerrar_bd ($id);
	}
	else {
		echo "&estado=error";
		echo "&mensaje=Operacion no definida: $operacion";
	}
} else {
	echo "&estado=error";
	echo "&mensaje=No se ha definido un tipo de operacion";
}
?>