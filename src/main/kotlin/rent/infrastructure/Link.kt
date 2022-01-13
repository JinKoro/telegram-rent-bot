package rent.infrastructure

object Link {
    const val ONLINER_MINSK = "https://r.onliner.by/sdapi/ak.api/search/apartments?only_owner=true&order=created_at%3Adesc&page=1&bounds%5Blb%5D%5Blat%5D=53.79496388914942&bounds%5Blb%5D%5Blong%5D=27.208980593159385&bounds%5Brt%5D%5Blat%5D=54.00965565644135&bounds%5Brt%5D%5Blong%5D=27.915699307659487&v=0.7672764437601876"
    const val KUFAR_MINSK = "https://cre-api.kufar.by/items-search/v1/engine/v1/search/rendered-paginated?prn=1000&size=30&sort=lst.d&typ=let&cat=1010&cur=USD&rnt=1&cmp=0&gtsy=country-belarus~province-minsk~locality-minsk"
    const val REALT_MINSK = "https://realt.by/api/mobile.2/list/?config%5Brecords-per-page%5D=40&config%5Bdate-format%5D=datetime&config%5Bthumb-width%5D=315&config%5Bcurrency%5D=&config%5Bthumb-height%5D=315&config%5Bpush%5D=0&config%5Btable%5D=rent-flats-for-long&config%5Bpage%5D=1&config%5Bpush_id%5D=&config%5Bsearch_id%5D=0&select%5Btown_id%5D%5Be%5D=5102&select%5Bstate_region_id%5D%5Be%5D=5&select%5Bx_only_private%5D%5Be%5D=1"
    const val IRR_MINSK = "http://irr.by/realestate/longtime/search/coord_polygon=27.481512042968752%2C53.97201643578927%2C27.39911458203126%2C53.943669843473415%2C27.382635089843763%2C53.87312440282724%2C27.536443683593767%2C53.81140050363853%2C27.723211261718767%2C53.856890134364455%2C27.679265949218763%2C53.96715839505669%2C27.481512042968752%2C53.97201643578927/currency=BYN/date_create=yesterday/list=list/page_len40/"
    const val NO_PHOTO_LINK = "https://www.vokrug.tv/pic/news/c/0/c/4/c0c4f3aee906751ae649c8541d677058.jpg"
    const val GOOGLE_MAPS = "https://www.google.com/maps/search/?api=1&query="
    const val KUFAR_PHOTO = "https://yams.kufar.by/api/v1/kufar-ads/images/"
}
