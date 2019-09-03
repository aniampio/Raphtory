package com.raphtory.core.analysis.AnalysisManager

import com.raphtory.core.analysis.Analyser

class BatchWindowAnalysisManager(jobID:String,analyser:Analyser,start:Long,end:Long,jump:Long,windows:Array[Long]) extends RangeAnalysisManager(jobID,analyser,start,end,jump) {
  override def windowSet(): Array[Long] = windows
}