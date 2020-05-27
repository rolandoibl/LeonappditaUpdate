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
import android.widget.Switch;
import android.widget.Toast;

import com.example.leonappditaupdate.Comida.Producto;
import com.example.leonappditaupdate.Cultura.CulturaBanner;
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
    EditText edtBannerCulturaLink;
    EditText edtBannerCulturaId;
    EditText edtBannerCulturaTitulo;
    EditText edtProductoProveedor;
    EditText edtProductoContacto;
    EditText edtProductoDescripcion;

    Button btnBanner1;
    Button btnBanner2;
    Button btnLibro;
    Button btnProducto;



    Button btnElegirImagen1;
    Button btnElegirImagenLibro;
    Button btnElegirImagenProducto;
    Button btnCultura;
    Button btnElegirImagenCultura;

    Switch swtDesayuno;
    Switch swtComida;
    Switch swtDulces;
    Switch swtPostres;


    ImageView imgvImagenItem;

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

        imgvImagenItem = findViewById(R.id.imgvBanner1);

        //Id producto
        edtProductoTitulo =findViewById(R.id.edtProductoTitulo);
        btnElegirImagenProducto=findViewById(R.id.btnElegirImagenProducto);
        btnProducto = findViewById(R.id.btnProducto);
        edtProductoContacto=findViewById(R.id.edtProductoContacto);
        edtProductoDescripcion=findViewById(R.id.edtProductoDescripcion);
        edtProductoProveedor=findViewById(R.id.edtProductoProveedor);
        swtDesayuno = findViewById(R.id.swtDesayuno);
        swtComida = findViewById(R.id.swtComida);
        swtDulces = findViewById(R.id.swtDulces);
        swtPostres = findViewById(R.id.swtPostres);

        //Id banner
        edtBanner1Link = findViewById(R.id.edtBanner1Link);
        btnBanner1 = findViewById(R.id.btnBanner1);
        btnBanner2 = findViewById(R.id.btnBanner2);
        btnElegirImagen1 = findViewById(R.id.btnElegirImagen1);
        btnBanner1.setOnClickListener(onClickBanner1);
        btnBanner2.setOnClickListener(onClickBanner2);

        //Id libro
        edtLibroLink = findViewById(R.id.edtLibroLink);
        edtLibroId = findViewById(R.id.edtLibroId);
        btnLibro = findViewById(R.id.btnLibro);
        btnElegirImagenLibro = findViewById(R.id.btnElegirImagenLibro);

        //Id banner cultura
        edtBannerCulturaLink=findViewById(R.id.edtBannerCulturaLink);
        edtBannerCulturaId=findViewById(R.id.edtBannerCulturaId);
        edtBannerCulturaTitulo=findViewById(R.id.edtBannerCulturaTitulo);
        btnCultura=findViewById(R.id.btnBannerCultura);
        btnElegirImagenCultura=findViewById(R.id.btnElegirImagenBannerCultura);

        //Listener
        btnLibro.setOnClickListener(onClickLibro);
        btnElegirImagen1.setOnClickListener(onClickElegirBanner1);
        btnElegirImagenLibro.setOnClickListener(onClickElegirLibro);
        btnElegirImagenProducto.setOnClickListener(onClickElegirProducto);
        btnProducto.setOnClickListener(onClickProducto);
        btnCultura.setOnClickListener(onClickSubirCultura);
        btnElegirImagenCultura.setOnClickListener(onClickElegirBannerCultura);

        //Instancia de Firebase
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
            AbrirGaleria();
        }
    };
    View.OnClickListener onClickElegirLibro = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AbrirGaleria();
        }
    };
    View.OnClickListener onClickElegirProducto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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

    View.OnClickListener onClickElegirBannerCultura= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AbrirGaleria();
        }
    };

    View.OnClickListener onClickSubirCultura= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AgregarBannerCultura();
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
                    imgvImagenItem.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),filepath);
                    imgvImagenItem.setImageBitmap(bitmap);

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
                                    imgvImagenItem.setImageDrawable(getResources().getDrawable(R.drawable.plussign));
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
                        String proveedor = edtProductoProveedor.getText().toString();
                        String contacto = edtProductoContacto.getText().toString();
                        String descripcion = edtProductoDescripcion.getText().toString();
                        //Libro miLibro = new Libro(imagen,link,Integer.parseInt(edtLibroId.getText().toString()));
                        Producto miProducto = new Producto(titulo,imagen,proveedor,contacto,descripcion);

                        //Obteniendo la coleccion a la cual se subira el producto
                        String coleccion = "Comida";
                        if(swtDesayuno.isChecked()){
                            coleccion = "Desayuno";
                        }
                        if(swtComida.isChecked()){
                            coleccion="Comida";
                        }
                        if(swtDulces.isChecked()){
                            coleccion="Dulces";
                        }
                        if(swtPostres.isChecked()){
                            coleccion= "Postres";
                        }

                        db.collection("Comida/Productos/"+coleccion).document(edtProductoTitulo.getText().toString()).set(miProducto).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"Producto actualizado",Toast.LENGTH_SHORT).show();
                                    //Limpiando campos
                                    imgvImagenItem.setImageDrawable(getResources().getDrawable(R.drawable.plussign));
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
                                    imgvImagenItem.setImageDrawable(getResources().getDrawable(R.drawable.plussign));
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


    private void AgregarBannerCultura(){
        //Subir imagen a Storage primero
        //Si ya se seleccionó una imagen de la galería
        if(filepathGlobal != null){
            final StorageReference filepathr = mStorageRef.child("bannersCultura").child("bannerCultura"+edtBannerCulturaId.getText()+".png");
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
                        link = edtBannerCulturaLink.getText().toString();
                        CulturaBanner miBanner = new CulturaBanner(imagen,link,edtBannerCulturaTitulo.getText().toString(),edtBannerCulturaId.getText().toString());
                        db.collection("Cultura/Banners/banners").document(edtBannerCulturaId.getText().toString()).set(miBanner).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"Banner actualizado",Toast.LENGTH_SHORT).show();
                                    //Limpiando campos
                                    imgvImagenItem.setImageDrawable(getResources().getDrawable(R.drawable.plussign));
                                    edtBannerCulturaLink.setText("");
                                    edtBannerCulturaId.setText("");
                                    edtBannerCulturaTitulo.setText("");
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

}
