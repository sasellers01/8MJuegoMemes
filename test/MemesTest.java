import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class MemesTest {
	
	@Test
	@DisplayName("Test carga de memes")
	void testCargaMemes() {

        Memes memes = new Memes();

        memes.cargarMemes();

        Meme meme = memes.obtenerMemeAleatorio();

        assertNotNull(meme, "No se ha obtenido ningún meme");

        assertNotNull(meme.getId(), "El meme no tiene ID");

        assertNotNull(meme.getTexto(), "El meme no tiene texto");
    }
}