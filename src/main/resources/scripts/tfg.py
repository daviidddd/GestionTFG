import os
from PyPDF2 import PdfReader
import re
import sys

def procesar_pdf(nombre_archivo):
    with open(nombre_archivo, 'rb') as pdf_file:
        # Crea un objeto lector de PDF
        pdf_reader = PdfReader(pdf_file)
        contador = 0

        # Itera sobre cada página del PDF
        for page_num in range(len(pdf_reader.pages)):
            # Extrae el texto de la página actual
            page = pdf_reader.pages[page_num]
            page_text = page.extract_text()

            # Directorio donde se guardarán los archivos
            directorio = "src/main/resources/tfgs"

            # Crea el directorio si no existe
            if not os.path.exists(directorio):
                os.makedirs(directorio)

            # Formateo - Eliminar cabecera
            inicio_delimitador = "Informática "
            page_text = re.sub(f".*?{re.escape(inicio_delimitador)}", "", page_text, flags=re.DOTALL)

            # Formateo - Eliminar dobles espacios
            page_text = page_text.replace("  ", " ")

            # Formateo - Eliminar saltos de linea
            page_text = page_text.replace("\n", " ")

            # Formateo - Separar TFG por linea
            page_text = page_text.replace("Nº", "\nNº")

            # Formateo - Eliminar tildes
            page_text = page_text.replace("á", "a")
            page_text = page_text.replace("é", "e")
            page_text = page_text.replace("í", "i")
            page_text = page_text.replace("ó", "o")
            page_text = page_text.replace("ú", "u")

            # Formateo - Palabras
            page_text = page_text.replace("Descr ipcion", "Descripcion")
            page_text = page_text.replace("Descripc ion", "Descripcion")
            page_text = page_text.replace("Desc ripcion", "Descripcion")

            lineas = page_text.split("\n")

            for linea in lineas:
                if linea.startswith("Nº"):
                    contador += 1
                    #directorio = "TFGs2"

                    # Crea el directorio si no existe
                    if not os.path.exists(directorio):
                        os.makedirs(directorio)

                    # Obtener el número del TFG
                    numero_tfg = linea.split()[1].strip()

                    # Crear el nombre del archivo
                    nombre_archivo = os.path.join(directorio, f"{contador}.txt")

                    # Guardar la línea en el archivo
                    with open(nombre_archivo, "a") as archivo:
                        archivo.write(linea + "\n")

if __name__ == "__main__":
    # Verificar que se haya proporcionado un nombre de archivo PDF como argumento
    if len(sys.argv) != 2:
        print("Uso: python script.py nombre_del_archivo.pdf")
        sys.exit(1)

    # Obtener el nombre del archivo PDF desde los argumentos de línea de comandos
    nombre_archivo_pdf = sys.argv[1]

    # Verificar si el archivo PDF existe
    if not os.path.exists(nombre_archivo_pdf):
        print("El archivo PDF especificado no existe.")
        sys.exit(1)

    # Procesar el archivo PDF
    procesar_pdf(nombre_archivo_pdf)

    print("Procesamiento completado.")
