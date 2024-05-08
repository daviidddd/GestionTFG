import os
import re

# Función para procesar el contenido de un archivo
def procesar_archivo(archivo):
    with open(archivo, 'r', encoding='utf-8') as f:
        contenido = f.read()

    # Definir las palabras clave para separar
    palabras_clave = ['Nº', 'Titulo', 'Tutor', 'Descripcion', 'Tecnologias', 'Asignaturas']

    # Crear un patrón regex para encontrar las palabras clave
    patron = '|'.join(palabras_clave)
    patron = r'({})'.format(patron)

    # Separar el contenido en líneas basadas en las palabras clave
    lineas = re.split(patron, contenido)

    # Eliminar elementos vacíos y espacios en blanco de las líneas
    lineas = [linea.strip() for linea in lineas if linea.strip()]

    # Escribir las líneas procesadas en un nuevo archivo
    nuevo_archivo = archivo.replace('.txt', '_formateado.txt')
    with open(nuevo_archivo, 'w', encoding='utf-8') as f:
        for i, linea in enumerate(lineas):
            if i % 2 == 0:
                f.write(linea)
            else:
                f.write('\n' + linea + '\n')


# Directorio que contiene los archivos .txt
#directorio = 'src/main/resources/tfgs'
directorio = os.path.join(os.path.expanduser("~"), "GestorUCAM", "tfgs")
directorio = os.path.abspath(directorio)

# Recorrer todos los archivos en el directorio
for archivo in os.listdir(directorio):
    if archivo.endswith('.txt'):
        procesar_archivo(os.path.join(directorio, archivo))
