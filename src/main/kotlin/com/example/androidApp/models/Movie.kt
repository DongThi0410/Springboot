package com.example.androidApp.models

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "movies")
class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0

    @Column(nullable = false, length = 255)
    var title: String = ""

    @Column(nullable = false)
    var des: String = ""

    @ManyToOne
    @JoinColumn(name = "genre_id", referencedColumnName = "id", nullable = false)
    var genre: Genre? = null

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    var cate: Category? = null

    @Column(nullable = false)
    var duration: Int = 0

    @Column(nullable = false, length = 255)
    var poster: String = ""

    @Column(nullable = false, length = 255)
    var cast: String = ""

    @Column(nullable = false)
    var rating: Float = 0.0f

    @Column(nullable = false, length = 255)
    var director: String = ""

    @Column(nullable = false)
    var start_date: LocalDate? = null

    @Column(nullable = false)
    var end_date: LocalDate? = null
}
