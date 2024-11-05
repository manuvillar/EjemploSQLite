package es.iesoretania.ejemplosqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",
                null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = binding.etCodigo.getText().toString();
        String producto = binding.etProducto.getText().toString();
        String precio = binding.etPrecio.getText().toString();

        if (!codigo.isEmpty() && !producto.isEmpty() && !precio.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("producto", producto);

            try {
                float precioFloat = Float.parseFloat(precio);
                registro.put("precio", precioFloat);

                BaseDeDatos.insert("articulos", null, registro);
                Toast.makeText(this, "Producto guardado correctamente",
                        Toast.LENGTH_SHORT).show();

                binding.etCodigo.setText("");
                binding.etProducto.setText("");
                binding.etPrecio.setText("");
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Formato de precio incorrecto",
                        Toast.LENGTH_SHORT).show();
            } finally {
                //BaseDeDatos.close();
            }
        } else {
            Toast.makeText(this, "Debes introducir todos los campos",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void Buscar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",
                null, 1);
        SQLiteDatabase BaseDeDatos = admin.getReadableDatabase();

        String codigo = binding.etCodigo.getText().toString();

        if (!codigo.isEmpty()) {
            Cursor fila = BaseDeDatos.rawQuery(
                    "SELECT producto, precio FROM articulos WHERE codigo = ?", new String[]{codigo});

            if (fila.moveToFirst()) {
                binding.etProducto.setText(fila.getString(0));
                binding.etPrecio.setText(fila.getString(1));
            } else {
                Toast.makeText(this, "No existe el producto", Toast.LENGTH_SHORT).show();
            }
            fila.close();
            //BaseDeDatos.close();
        } else {
            Toast.makeText(this, "Debes introducir el código del producto",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void Modificar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",
                null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = binding.etCodigo.getText().toString();
        String producto = binding.etProducto.getText().toString();
        String precio = binding.etPrecio.getText().toString();

        if (!codigo.isEmpty() && !producto.isEmpty() && !precio.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("producto", producto);

            try {
                float precioFloat = Float.parseFloat(precio);
                registro.put("precio", precioFloat);

                int cantidad = BaseDeDatos.update("articulos", registro,
                        "codigo = ?", new String[]{codigo});

                if (cantidad == 1) {
                    Toast.makeText(this, "Registro modificado correctamente",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No existe el producto",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Formato de precio incorrecto",
                        Toast.LENGTH_SHORT).show();
            } finally {
                //BaseDeDatos.close();
            }
        } else {
            Toast.makeText(this, "Debes introducir todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void Eliminar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",
                null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = binding.etCodigo.getText().toString();

        if (!codigo.isEmpty()) {
            int cantidad = BaseDeDatos.delete("articulos", "codigo = ?",
                    new String[]{codigo});

            if (cantidad == 1) {
                Toast.makeText(this, "Registro eliminado correctamente",
                        Toast.LENGTH_SHORT).show();
                binding.etCodigo.setText("");
                binding.etProducto.setText("");
                binding.etPrecio.setText("");
            } else {
                Toast.makeText(this, "No existe el producto",
                        Toast.LENGTH_SHORT).show();
            }
            //BaseDeDatos.close();
        } else {
            Toast.makeText(this, "Debes introducir el código del producto",
                    Toast.LENGTH_SHORT).show();
        }
    }
}