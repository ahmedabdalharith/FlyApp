package com.example.flyapp.ui.theme.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.FileProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility class to handle PDF generation and downloading
 */
object PdfUtils {
    fun generateTicketPdf(
        activity: Activity,
        bookingReference: String,
        content: @Composable () -> Unit
    ): Uri? {
        try {
            // Create PDF document
            val pdfDocument = PdfDocument()

            // Set up dimensions for A4-like page
            val pageWidth = 1240
            val pageHeight = 1754

            // Create the ComposeView with proper lifecycle owners
            val composeView = ComposeView(activity).apply {
                // Set fixed size for the PDF
                layoutParams = android.view.ViewGroup.LayoutParams(pageWidth, pageHeight)

                // *** IMPORTANT: Set lifecycle owners from the activity ***
                // This fixes the "Cannot locate windowRecomposer" issue
                setViewTreeLifecycleOwner(activity.window.decorView.findViewTreeLifecycleOwner())
                setViewTreeSavedStateRegistryOwner(activity.window.decorView.findViewTreeSavedStateRegistryOwner())

                // Set content
                setContent {
                    content()
                }
            }

            // Add view to activity temporarily to ensure proper composition
            activity.findViewById<android.view.ViewGroup>(android.R.id.content).addView(composeView)

            // Measure and layout with specified dimensions
            composeView.measure(
                View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY)
            )
            composeView.layout(0, 0, pageWidth, pageHeight)

            // Create bitmap and draw the view onto it
            val bitmap = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            composeView.draw(canvas)

            // Remove the temporary view
            activity.findViewById<android.view.ViewGroup>(android.R.id.content).removeView(composeView)

            // Create a page for the PDF
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            // Draw bitmap on the PDF page
            page.canvas.drawBitmap(bitmap, 0f, 0f, null)
            pdfDocument.finishPage(page)

            // Create file path for the PDF
            val filename = "Ticket_${bookingReference}_${
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            }.pdf"

            // Get the app's private file directory
            val pdfDir = File(activity.getExternalFilesDir(null), "pdfs").apply {
                if (!exists()) mkdirs()
            }

            val filePath = File(pdfDir, filename)

            // Write the PDF to the file
            pdfDocument.writeTo(FileOutputStream(filePath))
            pdfDocument.close()

            // Return content URI via FileProvider
            return FileProvider.getUriForFile(
                activity,
                "${activity.packageName}.fileprovider",
                filePath
            )

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "Error creating PDF: ${e.message}", Toast.LENGTH_LONG).show()
            return null
        }
    }

    /**
     * Open a PDF file with an external viewer
     * @param context Application context
     * @param pdfUri URI of the PDF file to open
     */
    fun openPdf(context: Context, pdfUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(pdfUri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        // Check if there's an app that can handle this intent
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // Provide fallback if no PDF viewer is installed
            val openWithIntent = Intent(Intent.ACTION_VIEW)
            openWithIntent.data = pdfUri
            openWithIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.startActivity(Intent.createChooser(openWithIntent, "Open with"))
        }
    }
}