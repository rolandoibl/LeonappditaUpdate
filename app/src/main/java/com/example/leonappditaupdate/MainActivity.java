package com.example.leonappditaupdate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leonappditaupdate.Comida.Producto;
import com.example.leonappditaupdate.Inicio.Banner;
import com.example.leonappditaupdate.Inicio.Libro;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText edtBanner1Link;
    EditText edtLibroLink;
    EditText edtLibroId;
    EditText edtProductoTitulo;

    Button btnBanner1;
    Button btnBanner2;
    Button btnLibro;
    Button btnProducto;

    Button btnElegirImagen1;
    Button btnElegirImagenLibro;
    Button btnElegirImagenProducto;

    ImageView imgvBanner1;
    ImageView imgvLibro;

    FirebaseFirestore db;

    String imagen;
    String link;

    final int PICK_PHOTO = 7;
    private StorageReference mStorageRef;
    private Uri filepathGlobal = null;

    //1 para banner, 2 para libro, 3 para producto
    private int llamada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtProductoTitulo =findViewById(R.id.edtProductoTitulo);
        btnElegirImagenProducto=findViewById(R.id.btnElegirImagenProducto);
        btnProducto = findViewById(R.id.btnProducto);
        edtBanner1Link = findViewById(R.id.edtBanner1Link);
        edtLibroLink = findViewById(R.id.edtLibroLink);
        edtLibroId = findViewById(R.id.edtLibroId);
        btnBanner1 = findViewById(R.id.btnBanner1);
        btnBanner2 = findViewById(R.id.btnBanner2);
        btnLibro = findViewById(R.id.btnLibro);
        btnElegirImagen1 = findViewById(R.id.btnElegirImagen1);
        btnElegirImagenLibro = findViewById(R.id.btnElegirImagenLibro);
        imgvBanner1 = findViewById(R.id.imgvBanner1);
        imgvLibro = findViewById(R.id.imgvLibro);
        btnBanner1.setOnClickListener(onClickBanner1);
        btnBanner2.setOnClickListener(onClickBanner2);
        btnLibro.setOnClickListener(onClickLibro);
        btnElegirImagen1.setOnClickListener(onClickElegirBanner1);
        btnElegirImagenLibro.setOnClickListener(onClickElegirLibro);
        btnElegirImagenProducto.setOnClickListener(onClickElegirProducto);
        btnProducto.setOnClickListener(onClickProducto);
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    View.OnClickListener onClickBanner1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AgregarBanner("01");
        }
    };
    View.OnClickListener onClickBanner2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AgregarBanner("02");
        }
    };
    View.OnClickListener onClickElegirBanner1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            llamada = 1;
            AbrirGaleria();
        }
    };
    View.OnClickListener onClickElegirLibro = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            llamada = 2;
            AbrirGaleria();
        }
    };
    View.OnClickListener onClickElegirProducto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            llamada = 3;
            AbrirGaleria();
        }
    };
    View.OnClickListener onClickLibro = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AgregarLibro();
        }
    };
    View.OnClickListener onClickProducto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AgregarProducto();
        }
    };

    private void AbrirGaleria(){
        //Intent implícito de galería
        Intent intentGalery = new Intent();
        //Indicar extensión de imagen
        intentGalery.setType("image/*");
        intentGalery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentGalery,"Seleccionar Imagen"),PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PHOTO && resultCode == RESULT_OK && data!=null && data.getData() != null){
            Bitmap bitmap = null;
            //Extrae la dirección de memoria de tu celular
            Uri filepath = data.getData();
            filepathGlobal = filepath;
            //Con la imagen de la galería, convertir a bitmap y poner en ImageView
            if(Build.VERSION.SDK_INT >= 28){
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),filepath);
                try{
                    bitmap = ImageDecoder.decodeBitmap(source);
                    if(llamada==1) imgvBanner1.setImageBitmap(bitmap);
                    else if(llamada==2) imgvLibro.setImageBitmap(bitmap);
                    if (llamada==3) imgvLibro.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),filepath);
                    if(llamada==1) imgvBanner1.setImageBitmap(bitmap);
                    else if(llamada==2) imgvLibro.setImageBitmap(bitmap);
                    if(llamada==3) imgvLibro.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void AgregarLibro(){
        //Subir imagen a Storage primero
        //Si ya se seleccionó una imagen de la galería
        if(filepathGlobal != null){
            final StorageReference filepathr = mStorageRef.child("libros").child("libro"+edtLibroId.getText()+".png");
            filepathr.putFile(filepathGlobal).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw new Exception();
                    }
                    return filepathr.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Se subió el libro",Toast.LENGTH_SHORT).show();
                        Uri downloadLink = task.getResult();
                        imagen = downloadLink.toString();

                        //Obteniendo cadenas a subir
                        link = edtLibroLink.getText().toString();
                        Libro miLibro = new Libro(imagen,link,Integer.parseInt(edtLibroId.getText().toString()));
                        db.collection("Inicio/Libros/libros").document(edtLibroId.getText().toString()).set(miLibro).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"Libro actualizado",Toast.LENGTH_SHORT).show();
                                    //Limpiando campos
                                    imgvLibro.setImageDrawable(getResources().getDrawable(R.drawable.plussign));
                                    edtLibroLink.setText("");
                                    edtLibroId.setText("");
                                    filepathGlobal = null;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Uh oh! Algo salió mal... No se actualizaron los libros",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }


    private void AgregarProducto(){
        //Subir imagen a Storage primero
        //Si ya se seleccionó una imagen de la galería
        if(filepathGlobal != null){
            final StorageReference filepathr = mStorageRef.child("productos").child("producto"+edtProductoTitulo.getText()+".png");
            filepathr.putFile(filepathGlobal).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw new Exception();
                    }
                    return filepathr.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Se subió el producto",Toast.LENGTH_SHORT).show();
                        Uri downloadLink = task.getResult();
                        imagen = downloadLink.toString();

                        //Obteniendo cadenas a subir
                        String titulo = edtProductoTitulo.getText().toString();
                        //Libro miLibro = new Libro(imagen,link,Integer.parseInt(edtLibroId.getText().toString()));
                        Producto miProducto = new Producto(titulo,imagen);
                        db.collection("Comida/Productos/Dulces").document(edtProductoTitulo.getText().toString()).set(miProducto).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"Producto actualizado",Toast.LENGTH_SHORT).show();
                                    //Limpiando campos
                                    imgvLibro.setImageDrawable(getResources().getDrawable(R.drawable.plussign));
                                    edtProductoTitulo.setText("");
                                    filepathGlobal = null;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Uh oh! Algo salió mal... No se actualizaron los productos",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }



    private void AgregarBanner(final String bannerId){
        //Subir imagen a Storage primero
        //Si ya se seleccionó una imagen de la galería
        if(filepathGlobal != null){
            final StorageReference filepathr = mStorageRef.child("banners").child("banner"+bannerId+".png");
            filepathr.putFile(filepathGlobal).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw new Exception();
                    }
                    return filepathr.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Se subió el banner",Toast.LENGTH_SHORT).show();
                        Uri downloadLink = task.getResult();
                        imagen = downloadLink.toString();

                        //Obteniendo cadenas a subir
                        link = edtBanner1Link.getText().toString();
                        Banner miBanner = new Banner(imagen,link);
                        db.collection("Inicio/Banners/banners").document(bannerId).set(miBanner).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"Banner actualizado",Toast.LENGTH_SHORT).show();
                                    //Limpiando campos
                                    imgvBanner1.setImageDrawable(getResources().getDrawable(R.drawable.plussign));
                                    edtBanner1Link.setText("");
                                    filepathGlobal = null;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Uh oh! Algo salió mal... No se actualizaron los banners",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
}
