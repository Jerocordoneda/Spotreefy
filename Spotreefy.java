package com.mycompany.spotreefy;
/*INTEGRANTES: 
 -ELIAS JERONIMO CORDONEDA MARTINEZ
 -BRUNO BARRAZA
 */
                                    
import java.util.Scanner;

public class Spotreefy {
    
    public static void desplegarMenu2(){
        System.out.println("----------------------------------------------------------");
        System.out.println("|   ingrese 1 para agregar cancion                       |");
        System.out.println("|   ingrese 2 para crear una playlist                    |");
        System.out.println("|   ingrese 3 para agregar cancion por titulo a playlist |");
        System.out.println("|   ingrese 4 para agregar cancion por autor a playlist  |");
        System.out.println("|   ingrese 5 para eliminar una playlist                 |");
        System.out.println("|   ingrese 6 para seguir un usuario                     |");
        System.out.println("|   ingrese 7 para volver al menu anterior               |");
        System.out.println("----------------------------------------------------------");
    }
    public static void desplegarMenu1(){
        System.out.println("----------------------------");
        System.out.println("||       SPOTFREEFY       ||");
        System.out.println("----------------------------");
        System.out.println("--------------------------");
        System.out.println("| ingrese 1 para login   |");
        System.out.println("| ingrese 2 para crear   |");
        System.out.println("| ingrese 3 para mostrar |");
        System.out.println("| ingrese 4 para salir   |");
        System.out.println("--------------------------");
    }
    public static void menu (EstructuraSpotreefy arbol, String usuarios, String canciones, String listaPropias, String listaSeguidores){
        Scanner entrada = new Scanner(System.in);
        int opcion = 0;
        while (opcion < 4){
            desplegarMenu1();
            opcion = entrada.nextInt();
            if (opcion == 1){
                System.out.println("Nombre de usuario: ");
                String nombreUsuario = entrada.next();
                System.out.println("Contrasenia: ");
                String password = entrada.next();
                NodoArbUsuarios usuario = arbol.login(nombreUsuario, password);
                int opcion2 = 0;
                if (usuario!= null) {
                    while (opcion2 < 7){
                        System.out.println("----------------------------------------");
                        System.out.println("Nombre de Usuario: " + usuario.getNombreUsuario());
                        arbol.mostrarPlaylist(usuario);
                        arbol.mostrarListaSeguidores(usuario);
                        System.out.println("---------------------------------------");    
                        desplegarMenu2();
                        opcion2 = entrada.nextInt();
                    if (opcion2 == 1){
                        System.out.println("Ingresar titulo de la cancion");
                        String titulo = entrada.next();
                        System.out.println("Ingresar autor de la cancion");
                        String autor = entrada.next();
                        if(titulo.length() <= 30  && autor.length() <= 8){
                            arbol.agregarCancionAutor(titulo, autor);
                        }else{
                            System.out.println("el titulo debe tener menos de 30 caracteres y el autor menos de 8");
                        }
                        arbol.mostrarArbolCanciones(); 
                        arbol.mostrarListaAutores();
                    }
                    if (opcion2 == 2){
                        System.out.println("Introduce el nuevo nombre de la Playlist");
                        String nombrePlaylist = entrada.next();
                        if(nombrePlaylist.length() <= 8){
                            arbol.crearListaPropia(usuario,nombrePlaylist);
                        }else{
                            System.out.println("el nombre de la playlist debe tener menos de 8 caracteres");
                        }
                    }
                    if (opcion2 == 3){
                        System.out.println("Introduce el nombre de la cancion que quieres agregar a la playlist");
                        String titulo = entrada.next();
                        System.out.println("Introduce el nombre de la playlist que desea agregar esa cancion");
                        String nombrePlaylist = entrada.next();
                        arbol.AgregarCancionesPropias(usuario, titulo, nombrePlaylist);
                        arbol.mostrarPlaylistsUsuario(usuario);
                    }
                    if (opcion2 == 4){
                        
                         System.out.println("Introduce el nombre de la playlist que desea agregar la cancion");
                         String nombrePlaylist = entrada.next();
                         System.out.println("Introduce el nombre del autor");
                         String nombreAutor = entrada.next();
                         System.out.println("las canciones de "+nombreAutor+" son: ");
                         arbol.agregarCancionPropiasPorAutor(usuario, nombrePlaylist, nombreAutor, entrada);
                         arbol.mostrarPlaylistsUsuario(usuario);
                    }
                    if (opcion2 == 5){
                        System.out.println("Nombre de la playlist q desea eliminar");
                        String nombrePlaylist = entrada.next();
                        arbol.EliminarListaPropia(usuario, nombrePlaylist);
                    }
                    if (opcion2 == 6)
                        arbol.SeguirUsuarioPorPlaylist(usuario, entrada);
                    }
                }
                
            }
            if (opcion == 2){
                System.out.println("Ingresar nombre");
                String nombreUsuario = entrada.next();
                System.out.println("Ingresar contrasenia");
                String password = entrada.next();
                arbol.crearUsuario(nombreUsuario, password);
            }
            if (opcion == 3){
                System.out.println("los usuarios existentes son: ");
                arbol.mostrarArbolUsuarios();
            }
            if (opcion == 4){
                System.out.println("Saliendo del programa...");
                arbol.SerializarUsuarios(usuarios);
                arbol.SerializarAutores(canciones);
                arbol.SerializarCanciones(canciones);
                arbol.serializarListaPropias(arbol.raizUsuarios.getListaPropias(),listaPropias);
                arbol.serializarListaSeguidores(arbol.raizUsuarios.getListaSeguidores(), listaSeguidores);
                entrada.close();
                }
            }
        }
        
        public static void main(String[] args) {
            EstructuraSpotreefy arbol = new EstructuraSpotreefy();
            String usuarios = "archusuarios";
            String autor = "archCanciones";
            String listaPropias =  "archListasPropias";
            String listaSeguidores = "archListasSeguidas";
            arbol.deserializarUsuarios(usuarios);
            arbol.deserializarAutores(autor);
            arbol.deserializarCanciones(autor);
            NodoListaPropias playlists = arbol.deserializarListaPropias(listaPropias);
            NodoListaSeguidores seguidores = arbol.deserializarSeguidores(listaSeguidores);
            if (arbol.raizUsuarios != null){
                arbol.raizUsuarios.setListaPropias(playlists);
                arbol.raizUsuarios.setListaSeguidores(seguidores);
            }
            menu(arbol, usuarios,autor,listaPropias,listaSeguidores);
        }
}
