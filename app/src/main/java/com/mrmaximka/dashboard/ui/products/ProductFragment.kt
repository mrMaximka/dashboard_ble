package com.mrmaximka.dashboard.ui.products

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.R
import com.mrmaximka.dashboard.model.BrandModel
import com.mrmaximka.dashboard.model.CategoryModel
import com.mrmaximka.dashboard.model.ProductModel
import com.mrmaximka.dashboard.ui.products.lists.BrandAdapter
import com.mrmaximka.dashboard.ui.products.lists.CategoryAdapter
import com.mrmaximka.dashboard.ui.products.lists.ProductAdapter
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_product.*
import kotlin.collections.ArrayList

class ProductFragment : Fragment(), UpdateInterface {

    private lateinit var brandList: RecyclerView
    private lateinit var catList: RecyclerView
    private lateinit var productList: RecyclerView
    private lateinit var brandAdapter: BrandAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var btnAddBrand: Button
    private lateinit var btnAddCategory: Button
    private lateinit var btnAddProduct: Button
    private var categoryList: ArrayList<CategoryModel> = ArrayList()
    private var allBrandList: ArrayList<BrandModel> = ArrayList()
    private var allProductList: ArrayList<ProductModel> = ArrayList()

    private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        Handler().postDelayed({
            initView()
            setViewSettings()
            loadBrands()
            loadCategories()
            loadProducts()
        }, 200)

    }

    @SuppressLint("CheckResult")
    private fun loadProducts() {
        val loadProducts = Runnable {
            Log.d("MMV", "Loading")
            allProductList = viewModel.loadProducts(context)
        }

        Completable.fromRunnable(loadProducts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(productsObserver)

    }

    @SuppressLint("CheckResult")
    private fun loadCategories() {
        val loadCategories = Runnable {
            Log.d("MMV", "Loading")
            categoryList = viewModel.loadCategories(context)
        }

        Completable.fromRunnable(loadCategories)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(categoryObserver)
    }

    @SuppressLint("CheckResult")
    private fun loadBrands() {
        val loadBrands = Runnable {
            Log.d("MMV", "Loading")
            allBrandList = viewModel.loadBrands(context)
        }

        Completable.fromRunnable(loadBrands)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(brandObserver)
    }

    private fun setViewSettings() {
        brandAdapter = BrandAdapter(context, this)
        brandList.adapter = brandAdapter

        categoryAdapter = CategoryAdapter(context, this)
        catList.adapter = categoryAdapter

        productAdapter = ProductAdapter(context, this)
        productList.adapter = productAdapter

        btnAddBrand.setOnClickListener { addBrand() }
        btnAddCategory.setOnClickListener { addCategory() }
        btnAddProduct.setOnClickListener { addProduct() }
    }

    private fun addProduct() {
        viewModel.addProduct(productAdapter)
    }

    private fun addCategory() {
        viewModel.addCategory(categoryAdapter)

    }

    private fun addBrand() {
        viewModel.addBrand(brandAdapter)
    }

    private fun initView() {
        brandList = view!!.findViewById(R.id.brand_list)
        catList = view!!.findViewById(R.id.cat_list)
        productList = view!!.findViewById(R.id.product_list)
        btnAddBrand = view!!.findViewById(R.id.add_brand_btn)
        btnAddCategory = view!!.findViewById(R.id.add_cat_btn)
        btnAddProduct = view!!.findViewById(R.id.add_product_btn)
    }

    private val productsObserver: CompletableObserver = object : CompletableObserver{
        override fun onComplete() {
            productAdapter.setElements(allProductList, categoryList, allBrandList)
            pbGoods.visibility = View.GONE
            product_list.visibility = View.VISIBLE
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при загрузке товаров", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }

    private val categoryObserver: CompletableObserver = object : CompletableObserver{
        override fun onComplete() {
            Log.d("MMV", "Complete")
            categoryAdapter.setElements(categoryList)
            pbCat.visibility = View.GONE
            cat_list.visibility = View.VISIBLE
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при загрузке категорий", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }

    private val brandObserver: CompletableObserver = object : CompletableObserver{
        override fun onComplete() {
            Log.d("MMV", "Complete")
            brandAdapter.setElements(allBrandList)
            pbBrand.visibility = View.GONE
            brand_list.visibility = View.VISIBLE
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при загрузке брендов", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }

    override fun updateBrands() {
        pbBrand.visibility = View.VISIBLE
        brand_list.visibility = View.GONE
        loadBrands()
        loadProducts()
    }

    override fun updateCategories() {
        pbCat.visibility = View.VISIBLE
        cat_list.visibility = View.GONE
        loadCategories()
        loadProducts()
    }

    override fun updateProducts() {
        pbGoods.visibility = View.VISIBLE
        product_list.visibility = View.GONE
        loadProducts()
    }
}