<?php
// Comprobamos que esta definido el tipo de operacion (add, del, edit, view)
if (isset($_GET['op'])) {
	include 'broker.php';
	$operacion = $_GET['op'];
	if ($operacion == "add") {
		if (isset($_GET['nombre'])) {
			$id = conectar_bd();
			$sql = "INSERT INTO H10_12_categoria_faq (categoria) VALUES ('".$_GET['nombre']."')";
			$res = ejecutar_consulta ($sql, $id);
			if ($res) {
				echo "&estado=ok";
				echo "&mensaje=La categoria ha sido creada con exito.";
			}
			else {
				echo "&estado=error";
				echo "&mensaje=No se ha podido crear la categoria. " . mysql_error();
			}
			cerrar_bd ($id);
		}
		else {
			echo "&estado=error";
			echo "&mensaje=Debe especificar el nombre de la categoria.";
		}
	}
	else if ($operacion == "view") {
		$id = conectar_bd();
		$sql = "SELECT categoria FROM H10_12_categoria_faq ORDER BY categoria";
		$res = ejecutar_consulta ($sql, $id);
		$contador = 0;
		while ($fila = mysql_fetch_array ($res)) {
			$categoria = $fila["categoria"];
			echo "&categoria$contador=$categoria";
			$contador++;
		}
		echo "&estado=ok";
		echo "&contador=$contador";
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