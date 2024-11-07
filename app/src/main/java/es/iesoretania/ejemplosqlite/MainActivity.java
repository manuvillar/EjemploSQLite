package es.iesoretania.ejemplosqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import es.iesoretania.ejemplosqlite.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void Registrar(View view) {
        // Crear una instancia de AdminSQLiteOpenHelper
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        // Obtener los valores de los campos de entrada
        String codigo = binding.etCodigo.getText().toString();
        String producto = binding.etProducto.getText().toString();
        String precio = binding.etPrecio.getText().toString();

        // Validar que los campos no estén vacíos
        if (!codigo.isEmpty() && !producto.isEmpty() && !precio.isEmpty()) {
            try {
                // Intentar convertir los valores a enteros y decimales
                int codigoInt = Integer.parseInt(codigo);
                double precioDouble = Double.parseDouble(precio);

                // Crear un objeto ContentValues para almacenar los valores
                ContentValues registro = new ContentValues();
                registro.put("codigo", codigoInt);
                registro.put("producto", producto);
                registro.put("precio", precioDouble);

                // Insertar el registro en la base de datos
                BaseDeDatos.insert("articulos", null, registro);

                // Limpiar los campos de entrada
                binding.etCodigo.setText("");
                binding.etProducto.setText("");
                binding.etPrecio.setText("");

                Toast.makeText(this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Código y precio deben ser numéricos", Toast.LENGTH_SHORT).show();
            } finally {
                BaseDeDatos.close();
            }
        } else {
            Toast.makeText(this, "Debes introducir todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void Buscar(View view) {
        // Crear una instancia de AdminSQLiteOpenHelper
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getReadableDatabase();

        // Obtener el valor del campo de entrada
        String codigo = binding.etCodigo.getText().toString();

        // Validar que el campo no esté vacío
        if (!codigo.isEmpty()) {
            // Realizar la consulta a la base de datos
            Cursor fila = BaseDeDatos.rawQuery("SELECT producto, precio FROM articulos WHERE codigo = ?",
                    new String[]{codigo});

            // Procesar el resultado de la consulta
            if (fila.moveToFirst()) {
                binding.etProducto.setText(fila.getString(0));
                binding.etPrecio.setText(fila.getString(1));
            } else {
                Toast.makeText(this, "No existe el producto", Toast.LENGTH_SHORT).show();
            }
            fila.close();
            BaseDeDatos.close();
        } else {
            Toast.makeText(this, "Debes introducir el código del producto", Toast.LENGTH_SHORT).show();
        }
    }

    public void Modificar(View view) {
        // Crear una instancia de AdminSQLiteOpenHelper
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        // Obtener los valores de los campos de entrada
        String codigo = binding.etCodigo.getText().toString();
        String producto = binding.etProducto.getText().toString();
        String precio = binding.etPrecio.getText().toString();

        // Validar que los campos no estén vacíos
        if (!codigo.isEmpty() && !producto.isEmpty() && !precio.isEmpty()) {
            try {
                // Intentar convertir los valores a enteros y decimales
                int codigoInt = Integer.parseInt(codigo);
                double precioDouble = Double.parseDouble(precio);

                // Crear un objeto ContentValues para almacenar los valores
                ContentValues registro = new ContentValues();
                registro.put("producto", producto);
                registro.put("precio", precioDouble);

                // Actualizar el registro en la base de datos
                int cantidad = BaseDeDatos.update("articulos", registro,
                        "codigo = ?", new String[]{String.valueOf(codigoInt)});


                if (cantidad == 1) {
                    Toast.makeText(this, "Registro modificado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No existe el producto", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Código y precio deben ser numéricos", Toast.LENGTH_SHORT).show();
            } finally {
                BaseDeDatos.close();
            }
        } else {
            Toast.makeText(this, "Debes introducir todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void Eliminar(View view) {
        // Crear una instancia de AdminSQLiteOpenHelper
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        // Obtener el valor del campo de entrada
        String codigo = binding.etCodigo.getText().toString();

        // Validar que el campo no esté vacío
        if (!codigo.isEmpty()) {
            // Eliminar el registro de la base de datos
            int cantidad = BaseDeDatos.delete("articulos", "codigo = ?", new String[]{codigo});

            if (cantidad == 1) {
                Toast.makeText(this, "Registro eliminado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No existe el producto", Toast.LENGTH_SHORT).show();
            }
            BaseDeDatos.close();
        } else {
            Toast.makeText(this, "Debes introducir el código del producto", Toast.LENGTH_SHORT).show();
        }
    }
}