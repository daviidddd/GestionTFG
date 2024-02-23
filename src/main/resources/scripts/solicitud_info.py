import sys
import re
#import unidecode
from PyPDF2 import PdfReader

def procesar_pdf(nombre_archivo):
    # Lista para almacenar todo el texto extraído de las páginas del PDF
    texto_completo = []

    try:
        # Abre el archivo PDF
        with open(nombre_archivo, 'rb') as pdf_file:
            # Crea un objeto lector de PDF
            pdf_reader = PdfReader(pdf_file)

            # Itera sobre cada página del PDF
            for page_num in range(len(pdf_reader.pages)):
                # Extrae el texto de la página actual y lo agrega a la lista
                page = pdf_reader.pages[page_num]
                texto_completo.append(page.extract_text())

    except FileNotFoundError:
        print("El archivo especificado no existe.")
        return

    # Formateo 1: Convertir la lista de texto completo en una cadena de texto
    texto_completo = ' '.join(texto_completo)

    # Formateo 2: Eliminar tildes
    #texto_completo = unidecode.unidecode(texto_completo)
    texto_completo = texto_completo.replace("á", "a")
    texto_completo = texto_completo.replace("é", "e")
    texto_completo = texto_completo.replace("í", "i")
    texto_completo = texto_completo.replace("ó", "o")
    texto_completo = texto_completo.replace("ú", "u")

# Formateo 3: Reemplazar múltiples espacios en blanco por uno solo
    patron_espacios = re.compile(r'\s+')
    texto_completo = re.sub(patron_espacios, ' ', texto_completo)

    # Define el patrón para eliminar el texto innecesario desde el inicio hasta "Correo electrónico:"
    patron1 = re.compile(r'^.*?Correo electronico:', re.DOTALL)
    # Define el patrón para eliminar el texto desde "2. Experiencia profesional" hasta el final del texto
    patron3 = re.compile(r'2\. Experiencia profesional.*', re.DOTALL)
    patron2 = re.compile(r'Orden.*?Numero de TFG1', re.DOTALL)

    # Eliminar el texto innecesario desde el inicio hasta "Correo electrónico:"
    texto_completo = re.sub(patron1, '', texto_completo)
    # Eliminar el texto desde "2. Experiencia profesional" hasta el final del texto
    texto_completo = re.sub(patron3, '', texto_completo)
    texto_completo = re.sub(patron2, '', texto_completo)

    # Define un patrón para encontrar los datos relevantes
    patron_datos = re.compile(r'(\d+)º\s')

    # Reemplaza los números seguidos por una "o" por una cadena vacía
    texto_completo = re.sub(patron_datos, '', texto_completo)

    texto_completo = texto_completo.replace(" ", "")

    # Patrón para buscar la letra "C" mayúscula seguida de un número
    patron_C_mayus = re.compile(r'(C\d)')

    # Dividir el texto en una nueva línea cada vez que se encuentre una "C" mayúscula seguida de un número
    texto_limpio = re.sub(patron_C_mayus, r'\n\1', texto_completo)

    # Imprimir el texto modificado
    print(texto_limpio)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Uso: python script.py nombre_archivo.pdf")
    else:
        archivo_pdf = sys.argv[1]
        procesar_pdf(archivo_pdf)
