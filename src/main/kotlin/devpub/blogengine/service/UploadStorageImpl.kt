package devpub.blogengine.service

import devpub.blogengine.util.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

private const val FOLDER_NAME_LENGTH = 2
private const val FILE_NAME_LENGTH = 10

@Service
open class UploadStorageImpl @Autowired constructor(
    @Value("\${blog-engine.upload-dir}") private val uploadDir: String,
    private val randomStringGenerator: RandomStringGenerator
): UploadStorage {
    override fun store(file: MultipartFile): String {
        val extension = FileUtils.getExtension(file.originalFilename ?: error("Файл недоступен"))

        while(true) {
            val subDirs = randomStringGenerator.generate(FOLDER_NAME_LENGTH) + File.separator +
                          randomStringGenerator.generate(FOLDER_NAME_LENGTH) + File.separator +
                          randomStringGenerator.generate(FOLDER_NAME_LENGTH) + File.separator

            val dir = uploadDir + subDirs

            val fileName = randomStringGenerator.generate(FILE_NAME_LENGTH) +
                           if(extension != null) ".$extension" else ""

            val filePath = Path.of(dir + fileName)

            if(Files.notExists(filePath)) {
                Files.createDirectories(Path.of(dir))
                file.transferTo(filePath)
                return subDirs + fileName
            }
        }
    }

    override fun remove(filePath: String) {
        Files.deleteIfExists(Path.of(uploadDir + filePath))
    }
}