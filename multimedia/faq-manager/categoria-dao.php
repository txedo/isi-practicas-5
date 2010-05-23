<?php
// Comprobamos que está definido el tipo de operación (add, del, edit, view)
if (isset($_GET['op'])) {
	include 'broker.php';
	$operacion = $_GET['op'];
	if ($operacion == "add") {
		if (isset($_GET['nombre'])) {
			$id = conectar_bd();
			$sql = "INSERT INTO categoria_faq (categoria) VALUES ('".$_GET['nombre']."')";
			$res = ejecutar_consulta ($sql, $id);
			if ($res) echo "estado=ok";
			cerrar_bd ($id);
		}
	} else if ($operacion == "view") {
		$id = conectar_bd();
		$sql = "SELECT categoria FROM categoria_faq ORDER BY categoria";
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
	}else {
		echo "Operación inválida";
	}
} else {
	echo "Error: No se ha definido un tipo de operacion";
}
?>