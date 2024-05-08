import sys
import PyPDF2
import re

def extraer_texto_desde_pdf(pdf_path):
    # Abre el archivo PDF
    pdf_file = open(pdf_path, 'rb')

    # Crea un objeto lector de PDF
    pdf_reader = PyPDF2.PdfReader(pdf_file)

    # Inicializa una variable para almacenar el texto extraído
    pdf_text = ''

    # Recorre todas las páginas y extrae el texto
    for page in pdf_reader.pages:
        pdf_text += page.extract_text()

    # Cierra el archivo PDF
    pdf_file.close()

    return pdf_text

def formatear_texto(pdf_text):
    # Formateo 1
    inicio_delimitador1 = "Impresión de expediente"
    fin_delimitador1 = "Asig.Cic.Cic.Cur.Cur.Mod.Mod.Observaciones Observaciones"
    pdf_text = re.sub(f"{re.escape(inicio_delimitador1)}.*?{re.escape(fin_delimitador1)}", "", pdf_text, flags=re.DOTALL)

    # Formateo2
    cadena_eliminar_desde = "Créditos Créditos Superados Superados Matriculados y No superados Matriculados y No superados Créditos mínimos Créditos mínimos"
    pdf_text = re.sub(f"{re.escape(cadena_eliminar_desde)}.*", "", pdf_text, flags=re.DOTALL)

    # Formateo3
    pdf_text = pdf_text.replace('\n', ' ')

    # Formateo4 - Separar las asignaturas por líneas
    nuevo_texto = re.sub(r'(\d{4}/\d{2}-)', r'\n\1', pdf_text)

    # Formateo 5 - Separar caracteres numéricos de alfabéticos
    nuevo_texto2 = re.sub(r'(\d)([A-Za-z])', r'\1 \2', nuevo_texto)
    nuevo_texto3 = re.sub(r'([A-Za-z])(\d)', r'\1 \2', nuevo_texto2)

    # Formateo 6 - Separar créditos de gr
    nuevo_texto4 = re.sub(r'(\d\.\d)(\d)', r'\1 \2', nuevo_texto3)

    # Formateo 7 - LowerCase
    nuevo_texto4 = nuevo_texto4.lower()

    # Formateo 8 - Quitar tildes
    nuevo_texto4 = nuevo_texto4.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")

    return nuevo_texto4

if __name__ == "__main__":
    # Verificar que se proporcionen los argumentos adecuados
    if len(sys.argv) != 2:
        print("Uso: python script.py <nombre_del_documento.pdf>")
        sys.exit(1)

    # Obtener nombre de archivo desde los argumentos de línea de comandos
    pdf_path = sys.argv[1]

    # Extraer texto desde el PDF
    texto_desde_pdf = extraer_texto_desde_pdf(pdf_path)

    # Formatear el texto
    texto_formateado = formatear_texto(texto_desde_pdf)

    # Imprimir el texto formateado
    print(texto_formateado)
