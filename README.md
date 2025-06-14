
# Proyecto de Ciberseguridad: Cifrador/Descifrador de Archivos en Java

## 📌 Descripción general

Este proyecto corresponde a la **opción 5** del proyecto final del curso de Ciberseguridad. Consiste en el desarrollo de un programa en Java que permite **cifrar y descifrar archivos de cualquier tipo**, usando:

- El algoritmo **AES** (Advanced Encryption Standard) con una clave de **256 bits**.
- Derivación de la clave desde una contraseña usando **PBKDF2** con sal (salt).
- Verificación de integridad usando el algoritmo **SHA-256**.
  
La aplicación trabaja a través de un menú interactivo que permite seleccionar entre:
1. Cifrar un archivo.
2. Descifrar un archivo.
3. Salir del programa.

## ⚙️ Tecnologías usadas

- Lenguaje: **Java 17**
- Librerías criptográficas: incluidas en `javax.crypto`, `java.security`, y `java.nio`
- Sistema operativo compatible: **Windows**, **Linux**, o **macOS**

## 🛡️ Detalles técnicos

### 🔐 Cifrado de archivos

Al seleccionar la opción de cifrado:
1. El usuario proporciona:
   - La ruta completa del archivo a cifrar (ideal que sea un .txt).
   - Una **contraseña**.
2. El programa:
   - Genera una **sal (salt)** aleatoria.
   - Usa el algoritmo **PBKDF2WithHmacSHA256** con esa sal para derivar una clave de 256 bits a partir de la contraseña.
   - Cifra el archivo original usando el algoritmo **AES/CBC/PKCS5Padding** con un vector IV aleatorio.
   - Calcula el **hash SHA-256 del archivo original** para asegurar su integridad.
   - Escribe en el archivo de salida (`.enc`):
     - La sal.
     - El IV.
     - El hash del archivo original.
     - Los bytes del archivo **cifrados**.

### 🔓 Descifrado de archivos

Al seleccionar la opción de descifrado:
1. El usuario proporciona:
   - La ruta del archivo `.enc` a descifrar.
   - La **contraseña** usada durante el cifrado.
2. El programa:
   - Extrae la sal, el IV y el hash desde el archivo `.enc`.
   - Deriva nuevamente la clave usando PBKDF2 y la contraseña ingresada.
   - Descifra el contenido cifrado.
   - Calcula el **SHA-256 del archivo descifrado**.
   - Compara el hash calculado con el hash original.
   - Si coinciden, guarda el archivo descifrado con la extensión `.dec`.
   - Si **no coinciden** (por ejemplo, si la contraseña fue incorrecta), muestra un mensaje indicando integridad fallida o contraseña errónea.

## 📁 Estructura del proyecto

```
CryptoApp/
├── src/
│   ├── Main.java
│   └── crypto/
│       └── CryptoManager.java
│       └── PasswordKeyDeriver.java
│   └── io/
│       └── FileManager.java
│   └── utils/
│       └── HashUtil.java
├── README.md
```

## ✅ Requisitos previos

- Tener **Java 17** o superior instalado en el sistema.
- Tener configurado un entorno de desarrollo como **IntelliJ**, **Visual Studio**, o usar la terminal.

## ▶️ Cómo compilar y ejecutar

### En terminal:

```bash
javac -d bin src/crypto/CryptoManager.java src/Main.java
cd bin
java Main
```

### En IDE:
1. Importa el proyecto como un proyecto Java.
2. Asegúrate de que los paquetes y clases estén correctamente organizados.
3. Ejecuta la clase `Main`.

## 🧪 Ejemplos de uso

### 1. Cifrar archivo

```
1. Cifrar archivo
Ingrese el nombre del archivo: C:\Users\Usuario\Escritorio\informe.pdf
Ingrese su contraseña: ********
Archivo cifrado correctamente: informe.pdf.enc
```

### 2. Descifrar archivo

```
2. Descifrar archivo
Ingrese el nombre del archivo: C:\Users\Usuario\Escritorio\informe.pdf.enc
Ingrese su contraseña: ********
Archivo descifrado correctamente: informe.pdf.dec
```

## 🔒 Seguridad aplicada

- **PBKDF2** (Password-Based Key Derivation Function 2) con SHA-256 y 65,536 iteraciones.
- **Sal aleatoria** de 16 bytes para cada archivo.
- **IV aleatorio** de 16 bytes para asegurar variabilidad entre archivos cifrados.
- Verificación de integridad con **SHA-256**.
- Todo el contenido cifrado se guarda en un único archivo `.enc`.

## ❌ Manejo de errores

- Si se introduce una contraseña incorrecta al descifrar, el sistema lo detecta por medio del **hash SHA-256**.
- Si el archivo tiene errores o está manipulado, también se alerta al usuario.

## 📌 Limitaciones

- No se guarda el nombre original del archivo dentro del `.enc`, por lo que el archivo descifrado se guarda como `*.dec`.
- El programa no sobrescribe archivos existentes; se recomienda asegurarse de no tener archivos `*.dec` con el mismo nombre antes de descifrar.

## 📎 Créditos

Desarrollado por **Luis Eduardo Charria Meneses**  
Proyecto FInal – Curso de Ciberseguridad  
Universidad Icesi – 2025
