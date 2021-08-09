package com.wyc.message.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wyc.message.R


/**
 *作者： wyc
 * <p>
 * 创建时间： 2019/11/20 17:46
 * <p>
 * 文件名字： com.wyc.vivodemo
 * <p>
 * 类的介绍：
 */
class MainAdapter(private var mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_BALANCE = 0
        const val TYPE_FUNCTION = 1
        const val TYPE_OPERATING_INCOME = 2
        const val TYPE_SERVICE = 3
        const val TYPE_ACTIVITY = 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            TYPE_BALANCE -> TYPE_BALANCE
            TYPE_FUNCTION -> TYPE_FUNCTION
            TYPE_OPERATING_INCOME -> TYPE_OPERATING_INCOME
            TYPE_SERVICE -> TYPE_SERVICE
            else -> TYPE_ACTIVITY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_BALANCE -> BalanceViewHolder(inflater.inflate(R.layout.layout_home_balance, parent, false))
            TYPE_FUNCTION -> FunctionViewHolder(inflater.inflate(R.layout.layout_home_function, parent, false))
            TYPE_OPERATING_INCOME -> OperatingIncomeViewHolder(inflater.inflate(R.layout.layout_home_operating_income, parent, false))
            TYPE_SERVICE -> ServiceViewHolder(inflater.inflate(R.layout.layout_home_service, parent, false))
            else -> ActivityViewHolder(inflater.inflate(R.layout.layout_home_activity, parent, false))
        }
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, positon: Int) {
        when (holder) {
            is BalanceViewHolder -> {
                holder.tvBalance.text = 88.25.toString()
                holder.tvConversion.setOnClickListener { view ->
                    mBalanceListener?.invoke(view, "Conversion")
                }
                holder.tvCollectionCode.setOnClickListener { view ->
                    mBalanceListener?.invoke(view, "CollectionCode")
                }
                holder.tvScanCheck.setOnClickListener { view ->
                    mBalanceListener?.invoke(view, "ScanCheck")
                }
                holder.tvBank.setOnClickListener { view ->
                    mBalanceListener?.invoke(view, "Back")
                }
            }
            is FunctionViewHolder -> {
                //提现申请
                holder.tvWithdrawalApplication.setOnClickListener { view ->
                    mFunctionListener?.invoke(view, "WithdrawalApplication")
                }
                //提现账款
                holder.tvWithdrawalAccounts.setOnClickListener { view ->
                    mFunctionListener?.invoke(view, "WithdrawalAccounts")
                }
                //营收统计
                holder.tvRevenueStatistics.setOnClickListener { view ->
                    mFunctionListener?.invoke(view, "RevenueStatistics")
                }
                //发票信息
                holder.tvInvoiceInformation.setOnClickListener { view ->
                    mFunctionListener?.invoke(view, "InvoiceInformation")
                }
                //消费权限
                holder.tvConsumptionAuthority.setOnClickListener { view ->
                    mFunctionListener?.invoke(view, "ConsumptionAuthority")
                }
            }
            is ServiceViewHolder -> {
                holder.serviceRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                holder.serviceRecyclerView.setHasFixedSize(true)
                val adapter = HomeServiceAdapter(mContext)
                holder.serviceRecyclerView.adapter = adapter
                val pages = mutableListOf<String>()
                pages.add("https://pic3.zhimg.com/80/v2-5faa2ffcac1992a2663c8746abbde9ae_hd.jpg")
                pages.add("https://pic1.zhimg.com/80/v2-78b72fb37fbcd6224940b7f15d76ef64_hd.jpg")
                pages.add("https://pic4.zhimg.com/80/v2-84c93abead7d8744422af35167aeeb2b_hd.jpg")
                pages.add("https://pic3.zhimg.com/80/v2-5faa2ffcac1992a2663c8746abbde9ae_hd.jpg")
                pages.add("https://pic1.zhimg.com/80/v2-78b72fb37fbcd6224940b7f15d76ef64_hd.jpg")
                pages.add("https://pic4.zhimg.com/80/v2-84c93abead7d8744422af35167aeeb2b_hd.jpg")
                adapter.setPages(pages)

                adapter.setOnHomeServiceItemClickListener { view, position ->
                    Log.e("服务点击事件", "onItemClick: $position")
                    mServiceListener?.invoke(view, position)
                }
            }
            is OperatingIncomeViewHolder -> {
                holder.tvSettlementAccumulation.text = 100.58.toString()
                holder.tvXindouSettlement.text = 40.12.toString()
                holder.tvCashSettlement.text = 95.82.toString()
            }

            is ActivityViewHolder -> {
                holder.recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                holder.recyclerView.setHasFixedSize(true)
                val adapter = HomeActivityAdapter(mContext)
                holder.recyclerView.adapter = adapter
                val pages = mutableListOf<String>()
                pages.add("https://pic3.zhimg.com/80/v2-5faa2ffcac1992a2663c8746abbde9ae_hd.jpg")
                pages.add("https://pic1.zhimg.com/80/v2-78b72fb37fbcd6224940b7f15d76ef64_hd.jpg")
                pages.add("https://pic4.zhimg.com/80/v2-84c93abead7d8744422af35167aeeb2b_hd.jpg")
                pages.add("https://pic3.zhimg.com/80/v2-5faa2ffcac1992a2663c8746abbde9ae_hd.jpg")
                pages.add("https://pic1.zhimg.com/80/v2-78b72fb37fbcd6224940b7f15d76ef64_hd.jpg")
                pages.add("https://pic4.zhimg.com/80/v2-84c93abead7d8744422af35167aeeb2b_hd.jpg")
                adapter.setPages(pages)

                adapter.setOnHomeActivityItemClickListener { view, position ->
                    Log.e("活动点击事件", "onItemClick: $position")
                    mActivityListener?.invoke(view, position)
                }
            }
        }
    }

    private var mBalanceListener: ((view: View, text: String) -> Unit)? = null

    fun setOnBalanceItemClickListener(listener: (view: View, text: String) -> Unit) {
        mBalanceListener = listener
    }

    inner class BalanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBalance: TextView = itemView.findViewById(R.id.tv_balance)
        val tvConversion: TextView = itemView.findViewById(R.id.tv_conversion)
        val tvCollectionCode: TextView = itemView.findViewById(R.id.tv_collection_code)
        val tvScanCheck: TextView = itemView.findViewById(R.id.tv_scan_check)
        val tvBank: TextView = itemView.findViewById(R.id.tv_bank)
    }

    private var mFunctionListener: ((view: View, text: String) -> Unit)? = null

    fun setOnFunctionItemClickListener(listener: (view: View, text: String) -> Unit) {
        mBalanceListener = listener
    }

    inner class FunctionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvWithdrawalApplication: TextView = itemView.findViewById(R.id.tv_withdrawal_application)
        var tvWithdrawalAccounts: TextView = itemView.findViewById(R.id.tv_withdrawal_accounts)
        var tvRevenueStatistics: TextView = itemView.findViewById(R.id.tv_revenue_statistics)
        var tvInvoiceInformation: TextView = itemView.findViewById(R.id.tv_invoice_information)
        var tvConsumptionAuthority: TextView = itemView.findViewById(R.id.tv_consumption_authority)
    }


    private var mServiceListener: ((view: View, position: Int) -> Unit)? = null

    fun setOnServiceItemClickListener(listener: (view: View, position: Int) -> Unit) {
        mServiceListener = listener
    }

    inner class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var serviceRecyclerView: RecyclerView = itemView.findViewById(R.id.service_recyclerview)
    }


    inner class OperatingIncomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSettlementAccumulation: TextView = itemView.findViewById(R.id.tv_settlement_accumulation)
        var tvXindouSettlement: TextView = itemView.findViewById(R.id.tv_xindou_settlement)
        var tvCashSettlement: TextView = itemView.findViewById(R.id.tv_cash_settlement)
    }

    private var mActivityListener: ((view: View, position: Int) -> Unit)? = null

    fun setOnActivityItemClickListener(listener: (view: View, position: Int) -> Unit) {
        mActivityListener = listener
    }

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recyclerView: RecyclerView = itemView.findViewById(R.id.activity_recyclerview)
    }
}