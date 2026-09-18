package org.neteinstein.snap2sheet

import org.neteinstein.snap2sheet.data.repository.MockInvoiceRepository
import org.neteinstein.snap2sheet.domain.model.InvoiceStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class InvoiceRepositoryTest {

    @Test
    fun startDraftFromScan_publishesANeedsReviewDraft() {
        val repository = MockInvoiceRepository()

        val draft = repository.startDraftFromScan()

        assertEquals(InvoiceStatus.NEEDS_REVIEW, draft.status)
        assertEquals(draft, repository.draftInvoice.value)
    }

    @Test
    fun commitDraft_movesTheDraftIntoInvoicesAsSynced() {
        val repository = MockInvoiceRepository()
        val invoiceCountBefore = repository.invoices.value.size
        repository.startDraftFromScan()

        val saved = repository.commitDraft("Despesas 2026")

        assertNotNull(saved)
        assertEquals(InvoiceStatus.SYNCED, saved.status)
        assertEquals("Despesas 2026", saved.destinationSpreadsheetName)
        assertEquals(invoiceCountBefore + 1, repository.invoices.value.size)
        assertNull(repository.draftInvoice.value)
    }

    @Test
    fun commitDraft_withNoDraft_returnsNull() {
        val repository = MockInvoiceRepository()

        val saved = repository.commitDraft("Despesas 2026")

        assertNull(saved)
    }
}
