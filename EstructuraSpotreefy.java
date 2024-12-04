package com.mycompany.spotreefy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

public class EstructuraSpotreefy implements Serializable {
    private static final long serialVersionUID = 1L;
        NodoArbUsuarios raizUsuarios;
        NodoArbCanciones raizCanciones;
        NodoListaAutores listaAutores;
    
    public EstructuraSpotreefy (){
        raizUsuarios = null;
        raizCanciones = null;
        listaAutores = null;
    }
    
//////////////////////////////////////////////////MENU 1
    
    public void crearUsuario (String nombreUsuario, String password){
        if (password.length() < 6 || password.length() > 8 || nombreUsuario.length() > 8) {
            System.out.println("La contrasena ingresada debe tener entre 6 y 8 caracteres y el nombre usuario no debe pasar los 8.");
            return;
        }
        raizUsuarios = crearUsuarioRec(raizUsuarios, nombreUsuario, password);
    }
    
    private NodoArbUsuarios crearUsuarioRec(NodoArbUsuarios actual, String nombreUsuario, String password){
        if (actual == null ){
            actual = new NodoArbUsuarios (nombreUsuario, password);
            System.out.println("Usuario creado correctamente");
        }else if(verificarSiexisteUsuario(actual,nombreUsuario,password)){
                System.out.println("el usuario ingresado ya existe ");
        }else if (actual.getNombreUsuario().compareTo(nombreUsuario) > 0 ){
                actual.setMenores(crearUsuarioRec(actual.getMenores(), nombreUsuario, password)); 
        }else if (actual.getNombreUsuario().compareTo(nombreUsuario) <= 0){
                actual.setMayores(crearUsuarioRec(actual.getMayores(), nombreUsuario, password));
        }
        return actual;
    }
    
    public boolean verificarSiexisteUsuario(NodoArbUsuarios actual, String nombreUsuario, String password){
            if (actual == null){
                return false;
            }
            if (nombreUsuario.compareTo(actual.getNombreUsuario()) < 0){
                return verificarSiexisteUsuario(actual.getMenores(), nombreUsuario, password);
            }else if (nombreUsuario.compareTo(actual.getNombreUsuario()) > 0){
                return verificarSiexisteUsuario(actual.getMayores(), nombreUsuario, password);
            }
            return true;
    }
        
    public void mostrarArbolUsuarios(){
        mostrarRecArbolUsuarios(raizUsuarios);
    }
        
    private void mostrarRecArbolUsuarios(NodoArbUsuarios actual){
        if (actual != null){
            mostrarRecArbolUsuarios(actual.getMenores());
            System.out.println("el usuario es: " + actual.getNombreUsuario());
            mostrarRecArbolUsuarios(actual.getMayores());
        }
    }
    

    public NodoArbUsuarios login(String nombreUsuario, String password){
        return loginRec(raizUsuarios, nombreUsuario, password);
    }
      
    private NodoArbUsuarios loginRec(NodoArbUsuarios actual, String nombreUsuario, String password){
       if(actual == null){
           System.out.println("No existe ese Usuario");
           return actual;
       }
       else{
            if(actual.getNombreUsuario().equals(nombreUsuario)){
                if(actual.getPassword().equals(password)){
                    System.out.println("Usuario ingresado correctamente");
                    return actual;
                }
                else{
                    return null;
                }
            }
            else{
                if(actual.getNombreUsuario().compareTo(nombreUsuario) > 0){
                    return loginRec(actual.getMenores(), nombreUsuario, password);
                }
                else{
                    return loginRec(actual.getMayores(), nombreUsuario, password);
                }
            }
       } 
    }
///////////////////////////////////////////////////////MENU 2
    
////////////////////////////1
    public void agregarCancionAutor(String titulo, String autor) {
        NodoArbCanciones cancionNodo = buscarCancion(raizCanciones, titulo);
        NodoListaAutores autorNodo = obtenerAutorNodo(listaAutores, autor);
        if (autorNodo == null) {
            agregarAutor(null, autor);
            autorNodo = obtenerAutorNodo(listaAutores, autor); 
        }
        if (!verificarAutor(autorNodo, autor)) {
            agregarAutor(cancionNodo, autor);
            raizCanciones = agregarCancionRec(raizCanciones, titulo, autor);
        } else if (!verificarNombreCancion(cancionNodo, titulo)) {
            raizCanciones = agregarCancionRec(raizCanciones, titulo, autor);
            vincularNodos(autorNodo, buscarCancion(raizCanciones, titulo));
        } else {
            System.out.println("La cancion ya existe con ese autor.");
        }
    }
    
    private void agregarAutor(NodoArbCanciones arbolCanciones, String autor) {
        NodoListaAutores nuevoNodo = new NodoListaAutores(autor, arbolCanciones);
        if (listaAutores == null ||listaAutores.getAutor().compareTo(autor) > 0) {
            nuevoNodo.setSiguiente(listaAutores);
            listaAutores = nuevoNodo;
        } else {
            NodoListaAutores anterior = listaAutores;
            NodoListaAutores actual = listaAutores;
            while (actual != null && actual.getAutor().compareTo(nuevoNodo.getAutor()) < 0) {
                anterior = actual;
                actual = actual.getSiguiente();
            }
            nuevoNodo.setSiguiente(actual);
            anterior.setSiguiente(nuevoNodo);
        }
    }
    

    private NodoArbCanciones agregarCancionRec(NodoArbCanciones actual,String titulo, String autor){
        if (actual == null){
            actual = new NodoArbCanciones(titulo);
        }else if(actual.getTitulo().compareTo(titulo) > 0){
            actual.setMenores(agregarCancionRec(actual.getMenores(),titulo, autor));
        }else if(actual.getTitulo().compareTo(titulo) <= 0){
            actual.setMayores(agregarCancionRec(actual.getMayores(),titulo, autor));
        }
        
        return actual;
    }
    
    /// metodo para vincularNodosCircular
    private void vincularNodos(NodoListaAutores actual, NodoArbCanciones nodo) {
        if (nodo == null) {
            return;
        }
        if (actual.getArbolCanciones() == null) {
            actual.setArbolCanciones(nodo);
            nodo.setSigCancion(nodo);
        } else {
            NodoArbCanciones cancion = actual.getArbolCanciones();
            while (cancion.getSigCancion() != actual.getArbolCanciones()) {
                cancion = cancion.getSigCancion();
            }
            cancion.setSigCancion(nodo);
            nodo.setSigCancion(actual.getArbolCanciones()); 
        }
    }

    private boolean verificarAutor(NodoListaAutores actual, String autor){
        while (actual  != null ){
            if (actual.getAutor().equals(autor)){
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    private NodoListaAutores obtenerAutorNodo(NodoListaAutores actual,String autor) {
        while (actual != null) {
            if (actual.getAutor().equals(autor)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    private boolean verificarNombreCancion (NodoArbCanciones actual, String titulo){
        if(actual == null){
            return false;
        }else if(actual.getTitulo().equals(titulo)){
            return true;
        }else if (actual.getTitulo().compareTo(titulo) > 0){
            return verificarNombreCancion(actual.getMenores(),titulo);
        }else
            return verificarNombreCancion(actual.getMayores(),titulo);
    }
    
    private NodoArbCanciones buscarCancion(NodoArbCanciones actual, String titulo) {
        if (actual == null){
            return null;
        }    
        if (actual.getTitulo().equals(titulo)){
            return actual;
        }    
        if (actual.getTitulo().compareTo(titulo) < 0){
            return buscarCancion(actual.getMayores(), titulo);
        }else{
            return buscarCancion(actual.getMenores(), titulo);
        }
    }

    public void mostrarArbolCanciones() {
        System.out.println("Las canciones son: ");
        mostrarArbolCancionesRec(raizCanciones);
    }

    private void mostrarArbolCancionesRec(NodoArbCanciones actual) {
        if (actual != null) {
            mostrarArbolCancionesRec(actual.getMenores());
            System.out.println("Titulo: " + actual.getTitulo());
            mostrarArbolCancionesRec(actual.getMayores());
        }
    }

    public void mostrarListaAutores(){
        System.out.println("Los autores son: ");
        NodoListaAutores actual = listaAutores; 
        while (actual !=null){
            System.out.println("Autor: " + actual.getAutor());
            actual= actual.getSiguiente();
        }
    }
    
/////////////////////////////////2  
   
    public void crearListaPropia(NodoArbUsuarios usuario, String nombrePlaylist) {
        if (usuario == null) {
            System.out.println("El usuario no existe.");
        }else if (verificarPlaylist(usuario.getListaPropias(), nombrePlaylist)) {
            System.out.println("La playlist ya existe.");
        }else{ 
            usuario.setListaPropias(agregarListaPropia(usuario.getListaPropias(), nombrePlaylist));
            System.out.println("Playlist creada correctamente.");
        }
    }
    

    private boolean verificarPlaylist(NodoListaPropias actual, String nombrePlaylist) {
        while (actual != null) {
            if (actual.getNombrePlaylist().equals(nombrePlaylist)) {
                return true; 
            }
            actual = actual.getSigPlaylist();
        }
        return false;
    }
    
    private NodoListaPropias agregarListaPropia(NodoListaPropias actual, String nombrePlaylist) {
        NodoListaPropias nuevaPlaylist = new NodoListaPropias(nombrePlaylist);
        if (actual == null || actual.getNombrePlaylist().compareTo(nombrePlaylist) > 0) {
            nuevaPlaylist.setSigPlaylist(actual);
            return nuevaPlaylist;
        }
        NodoListaPropias anterior = actual;
        while (anterior.getSigPlaylist() != null && anterior.getSigPlaylist().getNombrePlaylist().compareTo(nombrePlaylist) < 0) {
            anterior = anterior.getSigPlaylist();
        }
            nuevaPlaylist.setSigPlaylist(anterior.getSigPlaylist());
            anterior.setSigPlaylist(nuevaPlaylist);
            return actual;
    }
     
    
    public void mostrarPlaylist(NodoArbUsuarios usuario) {
        if (usuario != null) {
            System.out.println("Las playlists de " + usuario.getNombreUsuario() + " son: ");
            NodoListaPropias actual = usuario.getListaPropias();
            if (actual == null) {
                System.out.println("No hay playlist creadas.");
                return;
            }
            while (actual != null) {
                System.out.println("Playlist " + actual.getNombrePlaylist());
                actual = actual.getSigPlaylist();
            }
        }
    }
    
    
    public void mostrarPlaylistsUsuario(NodoArbUsuarios usuario) {
        if (usuario == null) {
            System.out.println("El usuario no existe.");
            return;
        }

        NodoListaPropias actualPlaylist = usuario.getListaPropias();
        if (actualPlaylist == null) {
            System.out.println("El usuario no tiene playlists.");
            return;
        }

        System.out.println("Playlists del usuario " + usuario.getNombreUsuario() + ":");
        while (actualPlaylist != null) {
            System.out.println("Playlist: " + actualPlaylist.getNombrePlaylist());
            NodoSubListaCanciones actualCancion = actualPlaylist.getCanciones();
            if (actualCancion == null) {
                System.out.println("-esta vacia-");
            } else {
                while (actualCancion != null) {
                    System.out.println(" -Canción: " + actualCancion.getCancion().getTitulo());
                    actualCancion = actualCancion.getSig();
                }
            }
            actualPlaylist = actualPlaylist.getSigPlaylist();
        }
    }
    
/////////////////////////////////////3 
 
        
    private NodoSubListaCanciones agregarCancionALista(NodoSubListaCanciones actual, NodoArbCanciones cancion) {
        NodoSubListaCanciones nuevaCancion = new NodoSubListaCanciones(cancion);
        nuevaCancion.setSig(actual);
        return nuevaCancion;
    }   

    
    private boolean verificarCancionEnLista(NodoSubListaCanciones actual, String titulo) {
        while (actual != null) {
            if (actual.getCancion().getTitulo().equals(titulo)) {
                return true; 
            }
            actual = actual.getSig();
        }
        return false;
    }
    
    private NodoListaPropias buscaPlaylist(NodoListaPropias actual, String nombrePlaylist) {
        while (actual != null) {
            if (actual.getNombrePlaylist().equals(nombrePlaylist)) 
                return actual; 
            actual = actual.getSigPlaylist();
            }
            return null;
    }
        

    
    public void AgregarCancionesPropias (NodoArbUsuarios usuario ,String titulo, String nombrePlaylist){
        NodoArbCanciones canciones = buscarCancion(raizCanciones, titulo);
        if (canciones == null){
            System.out.println("No existe la cancion en el arbol");
            return;
        }
        NodoListaPropias playlist = buscaPlaylist(usuario.getListaPropias(), nombrePlaylist);
        if (playlist == null) {
            System.out.println("La playlist no existe.");
            return;
        }  
        if (verificarCancionEnLista(playlist.getCanciones(), canciones.getTitulo())) {
            System.out.println("La cancion ya está en la playlist.");
            return;
        }
        playlist.setCanciones(agregarCancionALista(playlist.getCanciones(), canciones));
        System.out.println("La cancion se agrego a la playlist correctamente.");
    }

    public void mostrarCancionPorAutor(NodoArbCanciones actual, NodoArbCanciones primero){
      
        if(actual != primero){
            System.out.println(actual.getTitulo());
            mostrarCancionPorAutor(actual.getSigCancion(), primero);
        }
    }
    
/////////////////////////////4
    
    private NodoSubListaCanciones insertarCancionPlaylist(NodoSubListaCanciones primero, NodoSubListaCanciones nodo){
        if(primero != null){
           nodo.setSig(primero); 
        }
        primero = nodo;
        return primero;
    }
    
    public void agregarCancionPropiasPorAutor(NodoArbUsuarios usuario, String nombrePlaylist, String nombreAutor, Scanner entrada){
        NodoListaPropias listaPropia = buscaPlaylist(usuario.getListaPropias(), nombrePlaylist);
        if(listaPropia != null){
            NodoListaAutores listaAutor = obtenerAutorNodo(listaAutores, nombreAutor);
            if(listaAutor != null){
                System.out.println(listaAutor.getArbolCanciones().getTitulo());
                mostrarCancionPorAutor(listaAutor.getArbolCanciones().getSigCancion(), listaAutor.getArbolCanciones());
                System.out.println("Introduzca el nombre de la cancion a agregar en la playlist");
                String nombreCancion = entrada.next();
                NodoArbCanciones arbolCanciones = buscarCancion(raizCanciones, nombreCancion);
                if(arbolCanciones != null){
                    if(!verificarCancionEnLista(listaPropia.getCanciones(), nombreCancion ) ){
                      NodoSubListaCanciones nuevoNodo = new NodoSubListaCanciones(arbolCanciones);
                      listaPropia.setCanciones(insertarCancionPlaylist(listaPropia.getCanciones(), nuevoNodo));
                    }
                    else{
                        System.out.println("La cancion ya existe en la playlist");  
                    }
                }
                else{
                    System.out.println("La cancion no existe");
                }
            }
            else{
                System.out.println("El autor no existe");
            }
        }
        else{
            System.out.println("La playlist no existe");
        }
    }    
    
///////////////////////////////5
    
    public void EliminarListaPropia(NodoArbUsuarios usuario, String nombrePlaylist){
        if (verificarPlaylist(usuario.getListaPropias(), nombrePlaylist)){
            eliminarPlaylist(usuario, nombrePlaylist);
        }else
            System.out.println("no existe una playlist con ese nombre");
    }
    
    private void eliminarPlaylist(NodoArbUsuarios usuario, String nombrePlaylist){
        NodoListaPropias actual = usuario.getListaPropias();
        NodoListaPropias anterior = null;
        while (actual != null && !actual.getNombrePlaylist().equals(nombrePlaylist)){
            anterior = actual;
            actual = actual.getSigPlaylist();
        }
        if (actual == usuario.getListaPropias()){
            usuario.setListaPropias(actual.getSigPlaylist());
        }else{
            anterior.setSigPlaylist(actual.getSigPlaylist());
        }
        System.out.println("Playlist eliminada correctamente");
    }
    
    
    
///////////////////////////////6
    
    public void SeguirUsuarioPorPlaylist(NodoArbUsuarios usuarioSeguidor, Scanner entrada) {
        System.out.println("Ingrese el nombre del usuario a seguir:");
        String nombreUsuarioSeguido = entrada.next();
        NodoArbUsuarios usuarioSeguido = buscarUsuario(raizUsuarios, nombreUsuarioSeguido);
        if (usuarioSeguido == null) {
            System.out.println("El usuario " + nombreUsuarioSeguido + " no existe.");
            return;
        }
        mostrarPlaylist(usuarioSeguido);
        System.out.println("Ingresa el nombre de la playlist que deseas seguir:");
        String nombrePlaylist = entrada.next();
        NodoListaPropias playlist = buscaPlaylist(usuarioSeguido.getListaPropias(), nombrePlaylist);
        if (playlist == null) {
            System.out.println("La playlist " + nombrePlaylist + " no existe en el usuario " + nombreUsuarioSeguido);
            return;
        }
        if (verificarPlaylistSeguidos(usuarioSeguidor.getListaSeguidores(), nombreUsuarioSeguido, nombrePlaylist)) {
            System.out.println("El usuario ya sigue esta playlist.");
            return;
        }
        usuarioSeguidor.setListaSeguidores(agregarListaSeguidos(usuarioSeguidor.getListaSeguidores(), nombreUsuarioSeguido, nombrePlaylist));
        System.out.println("Playlist agregada correctamente.");
    }
    
    private boolean verificarPlaylistSeguidos(NodoListaSeguidores actual, String nombreUsuarioSeguir, String nombrePlaylist) {
        while (actual != null) {
            if (actual.getUsuariosSeguidos().equals(nombreUsuarioSeguir) && actual.getListaPropiaSeguido().equals(nombrePlaylist)) {
                return true; 
            }
            actual = actual.getSig();
        }
        return false; 
    }
    
   private NodoListaSeguidores agregarListaSeguidos(NodoListaSeguidores actual, String nombreUsuarioSeguido, String nombrePlaylist) {
        NodoListaSeguidores nuevoSeguido = new NodoListaSeguidores(nombreUsuarioSeguido, nombrePlaylist);
        if (actual == null || actual.getUsuariosSeguidos().compareTo(nombreUsuarioSeguido) > 0) {
            nuevoSeguido.setSig(actual);
            return nuevoSeguido; 
        }
        NodoListaSeguidores recorrido = actual;
        while (recorrido.getSig() != null && (recorrido.getSig().getUsuariosSeguidos().compareTo(nombreUsuarioSeguido) < 0 || (recorrido.getSig().getUsuariosSeguidos().equals(nombreUsuarioSeguido) && recorrido.getSig().getListaPropiaSeguido().compareTo(nombrePlaylist) < 0))) {
            recorrido = recorrido.getSig();
        }
            nuevoSeguido.setSig(recorrido.getSig());
            recorrido.setSig(nuevoSeguido);
            return actual;
        }

    public void mostrarListaSeguidores(NodoArbUsuarios usuario) {
        NodoListaSeguidores actual = usuario.getListaSeguidores();
        if (actual == null) {
            System.out.println("El usuario no tiene seguidos.");
            return;
        }
        System.out.println("Lista de seguidores del usuario " + usuario.getNombreUsuario() + ":");
        while (actual != null) {
            System.out.println("- Usuario seguido: " + actual.getUsuariosSeguidos()+
                               ", Lista seguida: " + actual.getListaPropiaSeguido());
            actual = actual.getSig();
        }
    }

    private NodoArbUsuarios buscarUsuario(NodoArbUsuarios actual, String usuarioSeguir) {
        if (actual == null) {
            return actual;
        }else if (usuarioSeguir.compareTo(actual.getNombreUsuario()) < 0) {
            return buscarUsuario(actual.getMenores(), usuarioSeguir);
        } else if (usuarioSeguir.compareTo(actual.getNombreUsuario()) > 0) {
            return buscarUsuario(actual.getMayores(), usuarioSeguir);
        }
        return actual; 
    }
    
    
///////////////////////////////////////// IMPLEMENTACION DE ARCHIVOS //////////////////////////////////////////////////////////
     
    public void SerializarUsuarios (String usuarios){
       try (FileOutputStream fileOut = new FileOutputStream(usuarios);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)){
            out.writeObject(this);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    
    public void deserializarUsuarios(String usuarios) {
        try (FileInputStream fileIn = new FileInputStream(usuarios);
              ObjectInputStream in = new ObjectInputStream(fileIn)) {
             EstructuraSpotreefy arbol = (EstructuraSpotreefy) in.readObject();
             this.raizUsuarios = arbol.raizUsuarios; 
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
     public void SerializarAutores (String autor){
       try (FileOutputStream fileOut = new FileOutputStream(autor);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)){
            out.writeObject(this);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    
    public void deserializarAutores(String autor) {
        try (FileInputStream fileIn = new FileInputStream(autor);
              ObjectInputStream in = new ObjectInputStream(fileIn)) {
             EstructuraSpotreefy lista = (EstructuraSpotreefy) in.readObject();
             this.listaAutores = lista.listaAutores;  
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
     public void SerializarCanciones (String autor){
       try (FileOutputStream fileOut = new FileOutputStream(autor);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)){
            out.writeObject(this);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    
    public void deserializarCanciones(String autor) {
        try (FileInputStream fileIn = new FileInputStream(autor);
              ObjectInputStream in = new ObjectInputStream(fileIn)) {
             EstructuraSpotreefy arbol = (EstructuraSpotreefy) in.readObject();
             this.raizCanciones = arbol.raizCanciones; 
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void serializarListaPropias(NodoListaPropias lista, String listaPropias) {
        try (FileOutputStream fileOut = new FileOutputStream(listaPropias);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NodoListaPropias deserializarListaPropias(String listaPropias) {
        try (FileInputStream fileIn = new FileInputStream(listaPropias);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            NodoListaPropias lista = (NodoListaPropias) in.readObject(); 
            return lista;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void serializarListaSeguidores(NodoListaSeguidores listaSeguidores, String seguidores) {
        try (FileOutputStream fileOut = new FileOutputStream(seguidores);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(listaSeguidores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NodoListaSeguidores deserializarSeguidores(String seguidores) {
        try (FileInputStream fileIn = new FileInputStream(seguidores);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            NodoListaSeguidores listaSeguidores = (NodoListaSeguidores) in.readObject();
            return listaSeguidores;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
