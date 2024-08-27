package nz.ac.canterbury.seng303.myflashcardapp

import android.app.Application
import kotlinx.coroutines.FlowPreview
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import nz.ac.canterbury.seng303.myflashcardapp.datastore.dataAccessModule

class MainApplication : Application() {
    @OptIn(FlowPreview::class)
    override fun onCreate() {
        super.onCreate()

        // Start Koin for dependency injection
        startKoin {
            androidContext(this@MainApplication)
            modules(dataAccessModule)
        }
    }
}
