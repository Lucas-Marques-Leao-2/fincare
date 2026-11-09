package com.example.fincare.sample

import com.example.fincare.domain.Accommodation
import com.example.fincare.domain.Contact
import com.example.fincare.domain.Transaction

val sampleDataAccommodation = Accommodation(
    total = 1840.0,
    rating = 4.93,
    imageUrl = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=1200&q=80",
    location = "Copacabana, Rio de Janeiro",
    host = "Ana",
    dates = "3 – 8 nov"
)

val sampleDataAccommodations: List<Accommodation> = listOf(
    Accommodation(
        total = 1840.0,
        rating = 4.93,
        imageUrl = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=1200&q=80",
        location = "Copacabana, Rio de Janeiro",
        host = "Ana",
        dates = "3 – 8 nov"
    ),
    Accommodation(
        total = 960.0,
        rating = 4.81,
        imageUrl = "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=1200&q=80",
        location = "Pinheiros, São Paulo",
        host = "Lucas",
        dates = "12 – 15 nov"
    ),
    Accommodation(
        total = 9260.0,
        rating = 4.97,
        imageUrl = "https://images.unsplash.com/photo-1613490493576-7fde63acd811?auto=format&fit=crop&w=1200&q=80",
        location = "Jurerê, Florianópolis",
        host = "Marina",
        dates = "20 – 27 dez"
    ),
    Accommodation(
        total = 729.0,
        rating = 4.74,
        imageUrl = "https://images.unsplash.com/photo-1493809842364-78817add7ffb?auto=format&fit=crop&w=1200&q=80",
        location = "Savassi, Belo Horizonte",
        host = "Pedro",
        dates = "1 – 4 dez"
    ),
    Accommodation(
        total = 3160.0,
        rating = 4.99,
        imageUrl = "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=1200&q=80",
        location = "Trancoso, Bahia",
        host = "Julia",
        dates = "8 – 15 jan"
    ),
    Accommodation(
        total = 960.0,
        rating = 4.88,
        imageUrl = "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&w=1200&q=80",
        location = "Boa Viagem, Recife",
        host = "Rafael",
        dates = "18 – 22 nov"
    ),
    Accommodation(
        total = 960.0,
        rating = 4.69,
        imageUrl = "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=1200&q=80",
        location = "Centro, Curitiba",
        host = "Camila",
        dates = "5 – 7 dez"
    ),
    Accommodation(
        total = 960.0,
        rating = 4.95,
        imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=1200&q=80",
        location = "Praia do Forte, Mata de São João",
        host = "Bruno",
        dates = "26 dez – 2 jan"
    )
)

val sampleDataTransaction: Transaction = Transaction(
    description = "Netflix",
    category = "Apps",
    amount = 10.0,
    type = "Despesa",
    date = "07/09",
    bankCode = "260",
    bankName = "NU PAGAMENTOS S.A."
)

val sampleDataContact: Contact = Contact(
    name = "José",
    number = "(82) 99340-9784"
)

val sampleDataContacts: List<Contact> = listOf(
    Contact(
        name = "José",
        number = "(82) 99340-9784"
    ),
    Contact(
        name = "João",
        number = "(82) 99340-9783"
    ),
    Contact(
        name = "Pedro",
        number = "(82) 99340-9781"
    ),
    Contact(
        name = "Mariana",
        number = "(82) 99340-9785"
    ),
    Contact(
        name = "Aline",
        number = "(82) 99340-9787"
    ),
    Contact(
        name = "Lucas",
        number = "(82) 99340-9788"
    ),
)

val sampleDataInvestments: List<com.example.fincare.domain.Investment> = listOf(
    com.example.fincare.domain.Investment(name = "CDB Nubank", type = "Renda fixa", quantity = 2.0, averagePrice = 300.0, currentPrice = 312.40, month = 1, year = 2026),
    com.example.fincare.domain.Investment(name = "LCI Inter", type = "Renda fixa", quantity = 1.0, averagePrice = 600.0, currentPrice = 631.20, month = 3, year = 2026),
    com.example.fincare.domain.Investment(name = "PETR4", type = "Renda variável", quantity = 10.0, averagePrice = 36.50, currentPrice = 38.10, month = 2, year = 2026),
    com.example.fincare.domain.Investment(name = "ITUB4", type = "Renda variável", quantity = 8.0, averagePrice = 32.0, currentPrice = 30.75, month = 4, year = 2026),
    com.example.fincare.domain.Investment(name = "Tesouro Selic 2029", type = "Tesouro direto", quantity = 0.03, averagePrice = 15800.0, currentPrice = 16450.0, month = 5, year = 2026),
    com.example.fincare.domain.Investment(name = "Poupança Caixa", type = "Poupança", quantity = 1.0, averagePrice = 250.0, currentPrice = 258.0, month = 6, year = 2026),
    com.example.fincare.domain.Investment(name = "Bitcoin", type = "Cripto", quantity = 0.001, averagePrice = 380000.0, currentPrice = 410000.0, month = 6, year = 2026),
)

val sampleDataAccounts: List<com.example.fincare.domain.Account> = listOf(
    com.example.fincare.domain.Account(name = "Nubank", bankName = "NU PAGAMENTOS S.A.", kind = "Open finance", balance = 1433.0, main = true),
    com.example.fincare.domain.Account(name = "Banco Inter", bankName = "BANCO INTER S.A.", kind = "Manual", balance = 600.0),
    com.example.fincare.domain.Account(name = "Carteira", bankName = "Dinheiro em espécie", kind = "Manual", balance = 600.0),
    com.example.fincare.domain.Account(name = "XP Investimentos", bankName = "XP INVESTIMENTOS CCTVM S.A.", kind = "Manual", balance = 100.0),
)
