package com.example.muslim.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.example.muslim.R
import com.example.muslim.adapter.AyatAdapter
import com.example.muslim.model.ModelAyat
import com.example.muslim.model.ModelSurah
import com.example.muslim.networking.Api
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class DetailSurahActivity : AppCompatActivity() {

    var nomor: String? = null
    var nama: String? = null
    var arti: String? = null
    var type: String? = null
    var ayat: String? = null
    var keterangan: String? = null
    var modelSurah: ModelSurah? = null
    var ayatAdapter: AyatAdapter? = null
    var progressDialog: ProgressDialog? = null
    var modelAyat: MutableList<ModelAyat> = ArrayList()
    var mHandler: Handler? = null

    @SuppressLint("RestrictedApi")
    override fun  onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_surah)


        toolbar_detail.setTitle(null)
        setSupportActionBar(toolbar_detail)
        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mHandler = Handler()

        modelSurah = intent.getSerializableExtra("detailSurah") as ModelSurah
        if (modelSurah != null) {
            nomor = modelSurah!!.nomor
            nama = modelSurah!!.nama
            arti = modelSurah!!.arti
            type = modelSurah!!.type
            ayat = modelSurah!!.ayat
            keterangan = modelSurah!!.keterangan


            tvHeader.setText(nama)
            tvTitle.setText(nama)
            tvSubTitle.setText(arti)
            tvInfo.setText("$type - $ayat Ayat")

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) tvKet.setText(Html.fromHtml(keterangan,
                Html.FROM_HTML_MODE_COMPACT))
            else {
                tvKet.setText(Html.fromHtml(keterangan))
            }

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Mohon Tunggu")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Sedang menampilkan data...")

        rvAyat.setLayputManager(LinearLayoutManager(this))
        rvAyat.setHasFixedSize(true)

        listAyat()
        }

        private fun listAyat() {
            progressDialog!!.show()
            AndroidNetworking.get(Api.URL_LIST_AYAT)
                .addPathParameter("nomor", nomor)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener) {
                    override fun onResponse(response: JSONArray) {
                        for (i : Int in 0 untill response.length()) {
                            try {
                                progressDialog!!.dismiss()
                                val dataApi = ModelAyat()
                                val jsonObject: JSONObject! = response.getJSONObject(i)
                                dataApi.nomor = jsonObject.getString("nomor")
                                dataApi.arab = jsonObject.getString("ar")
                                dataApi.indo = jsonObject.getString("id")
                                dataApi.terjemahan = jsonObject.getString("tr")
                                modelAyat.add(dataApi)
                                showListAyat()
                            }catch (e : JSONException) {
                                e.printStackTrace()
                                Toast.makeText(this@DetailSurahActivity, "Gagal menampilkan data!",
                                    Toast.LENGTH_SHORT.show()
                            }
                        }

                        override fun onError(anError: ANError) {
                            progressDialog!!.dismiss()
                            Toast.makeText(
                                this@DetailSurahActivity, "Tidak ada jaringan internet!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }

                private fun showListAyat() {
                    ayatAdapter = AyatAdapter(this@DetailSurahActivity, modelAyat)
                    rvAyat!!.adapter = ayatAdapter
                }

            override fun onOptionsItemSelected(item: MenuItem): Boolean {
                if(item.itemId == android.R.id.home) {
                    finish()
                    return true
                }
            return super.onOptionsItemSelected(item)
            }
        }