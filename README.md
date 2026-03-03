## Reflection 1

### 1. Clean Code Principles
Dalam mengimplementasikan fitur "Edit Product" dan "Delete Product", saya telah menerapkan beberapa prinsip *Clean Code* untuk menjaga kualitas kode:

*  Saya menggunakan nama variabel dan fungsi yang deskriptif. Contohnya di `ProductController.java`, nama fungsi seperti `editProductPage`, `editProductPost`, dan `deleteProduct` secara jelas menggambarkan tujuannya (menampilkan halaman edit, memproses edit, dan menghapus produk), sehingga kode mudah dipahami tanpa perlu banyak komentar.
*  Saya memisahkan berdasarkan kegunaannya dengan jelas. `ProductController` hanya mengatur jalur komunikasi (HTTP request), `ProductService` menangani logika bisnis, dan `ProductRepository` fokus pada manipulasi data.
*  Di `ProductRepository.java`, saya menggunakan salah satu fitur Java, yaitu Lambda Expression untuk menghapus produk:
    ```java
    productData.removeIf(product -> product.getProductId().equals(id));
    ```
  Cara ini jauh lebih ringkas dan mudah dibaca dibandingkan menggunakan perulangan `for` loop.

### 2. Secure Coding Practices
Saya telah menerapkan beberapa praktik pemrograman aman:

*  Saya menggunakan UUID (`UUID.randomUUID()`) untuk ID produk, bukan integer berurut (1, 2, 3). Ini mencegah serangan **IDOR (Insecure Direct Object Reference)** karena ID produk menjadi sulit ditebak oleh pihak yang tidak bertanggung jawab.

### 3. Mistakes and Areas for Improvement
Setelah mengevaluasi kode awal saya, saya menemukan celah keamanan pada fitur **Delete Product** dan telah memperbaikinya.

**Kesalahan:**
Awalnya, saya membuat fitur hapus menggunakan metode `GET` (`@GetMapping`) dan tombol link sederhana (`<a>`) di HTML.

**Mengapa itu bermasalah:**
1.   Metode `GET` seharusnya hanya untuk mengambil data (read-only), bukan untuk mengubah atau menghapus data di server.
2.   Menggunakan `GET` untuk menghapus data membuat aplikasi rentan terhadap serangan **Cross-Site Request Forgery (CSRF)**. Penyerang bisa membuat link jebakan yang akan menghapus produk secara otomatis jika diklik oleh pengguna.

Saya telah memperbaiki kode tersebut dengan mengubah metodenya menjadi `POST`:
1.  **Di Controller:** Saya mengganti anotasi menjadi `@PostMapping("/delete")` agar hanya menerima permintaan POST.
2.  **Di View (HTML):** Saya mengubah tombol link (`<a>`) menjadi sebuah `<form>`:
    ```html
    <form th:action="@{/product/delete}" method="post">
        <input type="hidden" name="productId" th:value="${product.productId}" />
        <button type="submit" class="btn btn-danger btn-sm">Delete</button>
    </form>
    ```
Perubahan ini memastikan fitur hapus berjalan sesuai standar protokol HTTP dan lebih aman dari serangan CSRF sederhana.

## Reflection 2
Setelah menulis Unit Test, saya merasa lebih percaya diri dengan kualitas kode saya. Unit test bertindak sebagai pengaman yang memastikan bahwa setiap bagian kecil (unit) dari logika aplikasi berjalan sesuai harapan, terutama untuk skenario-skenario tepi (*edge cases*) yang mungkin terlewat saat pengujian manual.

Mengenai jumlah unit test dalam satu class, tidak ada angka pasti. Jumlah tes harus cukup untuk mencakup
1.  **Positive Scenarios**
2.  **Negative Scenarios**
3.  **Edge Cases**

Untuk memastikan unit test sudah cukup, kita bisa menggunakan **Code Coverage**. Namun, 100% Code Coverage tidak menjamin kode bebas dari bug atau error.
Code coverage hanya memastikan baris kode tersebut *pernah dijalankan*, tetapi tidak menjamin kebenaran logika bisnisnya.
Code coverage mungkin tidak mendeteksi bug yang disebabkan oleh kesalahan pemahaman requirement sistem.

Jika saya membuat functional test baru untuk memverifikasi jumlah item dalam daftar produk dengan cara menyalin (*copy-paste*) prosedur setup dan variabel instance yang sama dari `CreateProductFunctionalTest.java`, menurut saya itu akan menurunkan kualitas kode.

Masalah *Clean Code* yang terjadi adalah **Code Duplication**. Ini melanggar prinsip **DRY (Don't Repeat Yourself)**.

Saya menduplikasi konfigurasi setup seperti `@LocalServerPort`, `@Value base url`, dan method `@BeforeEach setup` ke dalam class baru. Jika di masa depan saya perlu mengubah logika setup (misalnya mengganti port atau konfigurasi base URL), saya harus mengubahnya di semua file test satu per satu. Ini tidak efisien dan rentan kesalahan (*error-prone*).


Solusi yang lebih bersih adalah dengan membuat **Base Test Class** (Inheritance).
1.  Buat satu class induk (misalnya `BaseFunctionalTest`) yang berisi semua konfigurasi umum (setup port, base URL, inisialisasi driver).
2.  Class test lainnya (`CreateProductFunctionalTest`, `ProductListFunctionalTest`, dll) cukup melakukan **extends** ke class induk tersebut.
3.  Dengan begitu, kode setup hanya ditulis satu kali dan bisa digunakan kembali, membuat kode lebih rapi dan mudah di-maintain.

## Reflection 2
**Live Deployment Koyeb:** nutty-jerrie-rafasya-org-bee1affd.koyeb.app/

### 1. Evaluasi Code Quality
Selama proses pengerjaan modul ini dan integrasi dengan SonarCloud, saya menemukan dan memperbaiki beberapa isu terkait kualitas kode (Code Quality) serta meningkatkan pengujian agar memenuhi standar *clean code*:

* Saya menemukan adanya *unused import* di dalam `ProductServiceImpl.java`. Strategi saya adalah menghapusnya karena import yang tidak digunakan akan menambah *cognitive load* bagi developer lain yang membaca kode, serta berpotensi menimbulkan kebingungan atau konflik di masa depan.
* Pada file `ProductControllerTest.java`, string `"redirect:list"` digunakan secara berulang kali. SonarCloud mendeteksinya sebagai *code smell*. Strategi saya adalah mengekstrak string tersebut menjadi sebuah konstanta `private static final String REDIRECT_LIST = "redirect:list";` sehingga kode lebih rapi dan meminimalisir kesalahan pengetikan (*typo*).
* Terdapat ketidaksesuaian penulisan antara nama file HTML (PascalCase, contoh: `CreateProduct.html`) dengan string yang dikembalikan atau diuji di Controller Test (camelCase, contoh: `createProduct`). Saya menyesuaikan unit test agar mengekspektasikan *PascalCase* yang benar, sehingga tidak terjadi *error* pada saat Spring Boot mencoba merender halaman.
* Saya menghapus karakter *backtick* ( \` ) yang terselip pada *hash integrity* Bootstrap di `CreateProduct.html` yang merusak CSS, serta memperbaiki teks *placeholder* yang salah (*copy-paste error*).
* SonarCloud mendeteksi penggunaan modifier `public` yang tidak perlu di dalam interface `ProductService.java`. Secara *default*, semua method di dalam interface Java sudah bersifat `public`. Strategi saya adalah menghapus kata kunci `public` tersebut agar kode lebih bersih dan ringkas.

Strategi utama saya dalam memperbaiki kode adalah dengan membaca log dari SonarCloud dan GitHub Actions secara teliti, memahami akar masalahnya (apakah itu *logic error* atau *code smell*), lalu memperbaikinya secara bertahap di branch terpisah (`module-2-exercise`) sebelum melakukan *merge*.

**Peningkatan Code Coverage:**

Saya berhasil meningkatkan *code coverage* secara signifikan pada beberapa komponen utama aplikasi dengan melengkapi *unit test*. Peningkatan tersebut meliputi:
* `ProductController`: dari 9% menjadi 100%
* `ProductService`: dari 6% menjadi 100%
* `ProductRepository`: dari 70% menjadi 100%
* `EshopApplication` (main class): dari 37% menjadi 100%

### 2. Evaluasi Implementasi CI/CD
Menurut saya, *workflows* GitHub Actions yang telah saya implementasikan pada proyek ini sudah memenuhi definisi *Continuous Integration* (CI) dan *Continuous Deployment* (CD).

**Mengapa memenuhi Continuous Integration (CI)?**
Saya telah mengonfigurasi *workflow* yang secara otomatis berjalan setiap kali ada *push* atau *pull request* ke repositori. *Workflow* ini melakukan kompilasi, menjalankan *unit test* menggunakan Gradle (JaCoCo), serta melakukan analisis kualitas dan keamanan kode menggunakan SonarCloud dan OSSF Scorecard. Hal ini memastikan bahwa setiap perubahan baru selalu diuji dan divalidasi secara otomatis, sehingga *bug* dapat dideteksi sejak dini sebelum digabungkan ke *branch* utama.

**Mengapa memenuhi Continuous Deployment (CD)?**
Saya juga telah membuat file `Dockerfile` dan menyiapkan mekanisme *pull-based deployment* menggunakan layanan PaaS Koyeb. Setiap kali ada perubahan kode yang di-*merge* ke branch `main`, Koyeb akan mendeteksi perubahan tersebut secara otomatis, mem-*build* image Docker, dan langsung melakukan *deployment* ke server *production*. Rantai otomatisasi ini berhasil menghapus kebutuhan untuk merilis aplikasi secara manual, mempercepat siklus rilis, dan memastikan bahwa versi terbaru di repositori selalu tersinkronisasi dengan yang ada di *live server*.

## Reflection 3
### 1. Explain what principles you apply to your project!
* **Single Responsibility Principle (SRP)**: Saya memisahkan tanggung jawab yang tumpang tindih pada *controller*. `CarController` kini murni hanya menangani *routing* HTTP untuk entitas `Car`, sementara `ProductController` menangani `Product`.
* **Open/Closed Principle (OCP)**: Saya menggunakan *interface* `CarService` dan `ProductService`. Dengan struktur ini, jika nanti ada kebutuhan untuk membuat implementasi *service* baru (misalnya menggunakan *database* asli alih-alih *in-memory list*), saya cukup membuat kelas implementasi baru tanpa perlu mengubah kode yang ada di `CarController` maupun `ProductController`.
* **Liskov Substitution Principle (LSP)**: Pada branch `before-solid`, `CarController` mewarisi `ProductController` (`extends ProductController`). Hal ini melanggar LSP karena `CarController` bukanlah sebuah `ProductController` dan pewarisan ini membuat *endpoint* produk terekspos di bawah *path* mobil. Saya memperbaikinya dengan menghapus pewarisan tersebut sehingga `CarController` berdiri secara independen.
* **Interface Segregation Principle (ISP)**: Saya memisahkan *interface* untuk masing-masing entitas (`CarService` dan `ProductService`). Klien (`CarController`) hanya bergantung pada metode-metode yang benar-benar ia butuhkan untuk mengelola mobil, tanpa terbebani dengan metode pengelolaan produk.
* **Dependency Inversion Principle (DIP)**: Saya mengubah cara *controller* bergantung pada *service*. Sebelumnya, `CarController` menginjeksi implementasi konkret (`private CarServiceImpl carservice`). Sekarang, *controller* bergantung pada abstraksi (`private final CarService carservice`).

### 2. Explain the advantages of applying SOLID principles to your project with examples.
* **Kemudahan Menambah Fitur Baru**: Kalau ada kebutuhan variasi *service* baru di masa depan (misalnya metode penyimpanan baru), saya **nggak perlu panik** harus membongkar kode lama yang sudah jalan. Karena *interface*-nya sudah ada, saya tinggal bikin kelas implementasi baru yang terhubung ke *interface* tersebut tanpa mengganggu kelas `main` atau *controller*. (OCP)
* **Mencegah Efek Samping yang Tidak Terduga**: Dengan memperbaiki pelanggaran LSP (menghapus `extends ProductController`), sistem *routing* Spring Boot menjadi lebih aman dan dapat diprediksi. Saya terhindar dari *bug* di mana *user* yang mengakses `/car/list` malah memicu *behavior* milik produk.
* **Kemudahan Pengujian**: Berkat penerapan DIP dan penggunaan antarmuka (`CarService`), saya bisa dengan sangat mudah melakukan *unit testing* pada `CarController`. Saya cukup menyuntikkan (*inject*) *mock object* dari `CarService` menggunakan Mockito tanpa perlu memikirkan logika *database* di lapisan *repository*.
* **Kode Lebih Terorganisir dan Fokus**: Dengan SRP, kelas-kelas menjadi lebih pendek dan memiliki satu tujuan utama. Jika terjadi kendala pada antarmuka penambahan mobil, saya langsung tahu bahwa masalahnya pasti berada di `CarController` atau `CreateCar.html`, sehingga proses *debugging* jauh lebih cepat.

### 3. Explain the disadvantages of not applying SOLID principles to your project with examples.
* **Sistem yang Kaku**: Jika saya tidak menerapkan DIP dan tetap membiarkan `CarController` bergantung pada `CarServiceImpl` (kelas konkret), sistem akan sangat kaku. Jika sewaktu-waktu konstruktor dari `CarServiceImpl` berubah, saya harus ikut mengubah kode di dalam `CarController`.
* **Risiko Kerusakan Skala Besar**: Mengabaikan SRP dan menggabungkan semua logika (mobil dan produk) ke dalam satu kelas raksasa akan menciptakan *God Object*. Jika saya memodifikasi fitur produk di dalam kelas tersebut, ada kemungkinan saya tanpa sengaja merusak fitur mobil yang sebelumnya berjalan normal.
* **Sulit Dikembangkan**: Tanpa penerapan OCP, menambahkan varian implementasi baru berarti saya harus mengedit atau menambahkan blok `if-else` panjang di dalam kelas *service* atau *controller* yang sudah ada. Mengubah kode yang sudah stabil di sistem *production* memiliki risiko tinggi untuk menimbulkan *bug* baru.