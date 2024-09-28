package io.legere.pdfiumandroid

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.legere.pdfiumandroid.base.BasePDFTest
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PdfDocumentTest : BasePDFTest() {
    private lateinit var pdfDocument: PdfDocument
    private var pdfBytes: ByteArray? = null

    @Before
    fun setUp() {
        pdfBytes = getPdfBytes("f01.pdf")

        assertNotNull(pdfBytes)

        pdfDocument = PdfiumCore().newDocument(pdfBytes)
    }

    @After
    fun tearDown() {
        pdfDocument.close()
    }

    @Test
    fun getPageCount() {
        val pageCount = pdfDocument.getPageCount()

        assert(pageCount == 4) { "Page count should be 4" }
    }

    @Test
    fun openPage() {
        val page = pdfDocument.openPage(0)

        assertNotNull(page)
    }

    @Test
    fun openPages() {
        val page = pdfDocument.openPages(0, 3)

        assert(page.size == 4) { "Page count should be 4" }
    }

    @Test
    fun getDocumentMeta() {
        val meta = pdfDocument.getDocumentMeta()

        assertNotNull(meta)
    }

    @Test
    fun getTableOfContents() {
        // I don't think this test document has a table of contents
        val toc = pdfDocument.getTableOfContents()

        assertNotNull(toc)
        assertThat(toc.size).isEqualTo(0)
    }

    @Test
    fun openTextPage() {
        val page = pdfDocument.openPage(0)
        val textPage = page.openTextPage()
        assertNotNull(textPage)
    }

//    @Test
//    fun openTextPages() {
//        val textPages = pdfDocument.openTextPages(0, 3)
//        assertThat(textPages.size).isEqualTo(4)
//    }

    @Test
    fun saveAsCopy() {
        pdfDocument.saveAsCopy(
            object : PdfWriteCallback {
                override fun WriteBlock(data: ByteArray?): Int {
                    // assertThat(data?.size).isEqualTo(pdfBytes?.size)
                    // assertThat(data).isEqualTo(pdfBytes)
                    return data?.size ?: 0
                }
            },
        )
    }

    @Test(expected = IllegalStateException::class)
    fun closeDocument() {
        var shouldBeClosed: PdfDocument?
        PdfiumCore().newDocument(pdfBytes).use { pdfDocument ->
            Assert.assertNotNull(pdfDocument)
            shouldBeClosed = pdfDocument
        }

        // Now it should be closed
        shouldBeClosed?.openPage(0) // This should throw an exception
    }
}
