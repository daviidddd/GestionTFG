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
    patron_final = re.compile(r'2\. Experiencia profesional.*', re.DOTALL)
    texto_completo = re.sub(patron1, '', texto_completo)
    texto_completo = re.sub(patron_final, '', texto_completo)

    # Separar creditos
    inicio_delimitador1 = "Orden"
    fin_delimitador1 = "TFG) "
    texto_completo = re.sub(f"{re.escape(inicio_delimitador1)}.*?{re.escape(fin_delimitador1)}", "", texto_completo,
                            flags=re.DOTALL)

    # Separar media
    inicio_delimitador1 = "Media"
    fin_delimitador1 = "academico"
    texto_completo = re.sub(f"{re.escape(inicio_delimitador1)}.*?{re.escape(fin_delimitador1)}", "", texto_completo,
                            flags=re.DOTALL)

    # Separar experiencia profesional
    inicio_delimitador1 = "Total"
    fin_delimitador1 = "profesional"
    texto_completo = re.sub(f"{re.escape(inicio_delimitador1)}.*?{re.escape(fin_delimitador1)}", "", texto_completo,
                            flags=re.DOTALL)

    # Separar meritos
    inicio_delimitador1 = "Otros"
    fin_delimitador1 = "(SI/NO)"
    texto_completo = re.sub(f"{re.escape(inicio_delimitador1)}.*?{re.escape(fin_delimitador1)}", "", texto_completo,
                            flags=re.DOTALL)

    # Separar el resto de TFGS
    texto_completo = re.sub(r'TFG[1-5]', '', texto_completo)

    # Separar preferencias
    inicio_delimitador1 = "1. Preferencias"
    fin_delimitador1 = "1º"
    texto_completo = re.sub(f"{re.escape(inicio_delimitador1)}.*?{re.escape(fin_delimitador1)}", "", texto_completo,
                            flags=re.DOTALL)

    # Separar el resto de TFGS
    texto_completo = re.sub(r'[2-5]º', '', texto_completo)

    # Espacio en blanco
    patron_espacios = re.compile(r'\s+')
    texto_completo = re.sub(patron_espacios, ' ', texto_completo)

    # Juntar codigo tfg
    patron_numeros_c = re.compile(r'(C\d+)\s*–\s*(\d+)')
    texto_completo = patron_numeros_c.sub(r'\1-\2', texto_completo)

    # Separamos en diferentes lineas
    texto_completo = texto_completo.replace(" ", "\n")

    # Imprimir el texto modificado
    print(texto_completo.lstrip())

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Uso: python script.py nombre_archivo.pdf")
    else:
        archivo_pdf = sys.argv[1]
        procesar_pdf(archivo_pdf)
