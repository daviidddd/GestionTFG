import sys
import PyPDF2
import re

def obtener_nia_desde_pdf(nombre_archivo):
    try:
        pdf_file = open(nombre_archivo, 'rb')

        # Crea un objeto lector de PDF
        pdf_reader = PyPDF2.PdfReader(pdf_file)

        # Inicializa una variable para almacenar el texto extraído
        pdf_text = ''

        # Recorre todas las páginas y extrae el texto
        for page in pdf_reader.pages:
            pdf_text += page.extract_text()

        # Cierra el archivo PDF
        pdf_file.close()

        # A minúscula
        pdf_text = pdf_text.lower()

        # Formato para eliminar desde el inicio hasta la primera ocurrencia de "NIA"
        inicio_delimitador = r"nia"
        pdf_text = re.sub(f".*?{re.escape(inicio_delimitador)}", "", pdf_text, flags=re.DOTALL)

        # Formateo para eliminar desde el inicio hasta la primera ocurrencia de "Centro Escuela"
        inicio_delimitador2 = r"centro escuela"
        pdf_text = re.sub(f"{re.escape(inicio_delimitador2)}.*", "", pdf_text, flags=re.DOTALL)

        # Eliminar todos los espacios en blanco
        pdf_text = pdf_text.replace(" ", "")

        # Convertir el texto restante a un número
        numero = int(pdf_text)

        return numero

    except Exception as e:
        return 0

# Comprobar si se proporcionó un nombre de archivo como argumento
if len(sys.argv) != 2:
    print("Uso: python script.py archivo.pdf")
    sys.exit(1)

# Obtener el nombre del archivo desde los argumentos de la línea de comandos
nombre_archivo = sys.argv[1]

# Obtener el número del PDF
numero = obtener_nia_desde_pdf(nombre_archivo)

# Imprimir el número o 0 si no se pudo obtener
print(numero)
