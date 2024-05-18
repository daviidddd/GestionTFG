# Gestor de Trabajos Fin de Grado (TFG)

La aplicación Gestor de TFG es una herramienta multiplataforma desarrollada en JavaFX y complementada con scripts en Python para la gestión de trabajos fin de grado. Esta aplicación facilita diversas tareas relacionadas con la administración de proyectos de fin de carrera, ofreciendo una interfaz intuitiva y funcionalidades eficientes.

## Requisitos Previos

Antes de utilizar la aplicación, asegúrate de tener instalados los siguientes componentes:

### Python

Para ejecutar los scripts Python integrados en la aplicación, asegúrate de tener Python instalado en tu sistema. Puedes instalarlo desde los siguientes enlaces:

- **MacOS**:
    - [Sitio web oficial de Python para MacOS](https://www.python.org/downloads/macos/)
    - [Instalación mediante Homebrew](https://docs.brew.sh/Homebrew-and-Python)

- **Windows**:
    - [Sitio web oficial de Python para Windows](https://www.python.org/downloads/windows/)

- **Linux**:
    - [Sitio web oficial de Python para Linux](https://www.python.org/downloads/source/)

### MySQL

La aplicación requiere una base de datos MySQL para almacenar y gestionar la información de los proyectos de TFG. Sigue estos pasos para instalar MySQL en tu sistema:

- **MacOS**:
    - [Sitio web oficial de MySQL para MacOS](https://dev.mysql.com/downloads/mysql/)
    - [Instalación mediante Homebrew](https://formulae.brew.sh/formula/mysql)

- **Windows**:
    - [Sitio web oficial de MySQL para Windows](https://dev.mysql.com/downloads/mysql/)

- **Linux**:
    - [Sitio web oficial de MySQL para Linux](https://dev.mysql.com/downloads/mysql/)

## Configuración Adicional

Una vez instalados Python y MySQL, necesitarás realizar algunas configuraciones adicionales:

### Configuración de Python

La ejecución de scripts relacionados con el procesamiento de documentos PDF requiere la instalación de la librería PyPDF2. Puedes instalarla utilizando pip:

```bash
pip install PyPDF2
```

### Creación de la base de datos en MySQL

Gran parte de la información será gestionada mediante base de datos por lo que es necesario su creación.

```bash 
mysql -u tu_usuario -p tu_base_de_datos < ~/GestorUCAM/scripts/bbdd.sql
```

