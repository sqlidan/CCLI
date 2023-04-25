<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:x="urn:schemas-microsoft-com:office:excel"
 xmlns:dt="uuid:C2F41010-65B3-11d1-A29F-00AA00C14882"
 xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:html="http://www.w3.org/TR/REC-html40">
 <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
  <Author>wanggw</Author>
  <LastAuthor>Windows 用户</LastAuthor>
  <Revision>1</Revision>
  <LastPrinted>2016-04-21T09:21:00Z</LastPrinted>
  <Created>2016-04-20T05:59:12Z</Created>
  <LastSaved>2019-04-20T01:39:35Z</LastSaved>
  <Version>12.00</Version>
 </DocumentProperties>
 <CustomDocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
  <KSOProductBuildVer dt:dt="string">2052-10.1.0.5975</KSOProductBuildVer>
 </CustomDocumentProperties>
 <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
  <WindowHeight>9510</WindowHeight>
  <WindowWidth>20730</WindowWidth>
  <WindowTopX>0</WindowTopX>
  <WindowTopY>0</WindowTopY>
  <ProtectStructure>False</ProtectStructure>
  <ProtectWindows>False</ProtectWindows>
 </ExcelWorkbook>
 <Styles>
  <Style ss:ID="Default" ss:Name="Normal">
   <Alignment ss:Vertical="Center"/>
   <Borders/>
   <Font ss:FontName="等线" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
   <Interior/>
   <NumberFormat/>
   <Protection/>
  </Style>
  <Style ss:ID="s64">
   <NumberFormat ss:Format="0;[Red]0"/>
  </Style>
  <Style ss:ID="s65">
   <NumberFormat ss:Format="0.00;[Red]0.00"/>
  </Style>
  <Style ss:ID="s76">
   <Borders/>
   <Font ss:FontName="等线" x:CharSet="134" ss:Size="11" ss:Color="#000000"
    ss:Bold="1"/>
   <NumberFormat ss:Format="0.00;[Red]0.00"/>
  </Style>
  <Style ss:ID="s77">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Font ss:FontName="等线" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
  </Style>
  <Style ss:ID="s83">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Font ss:FontName="等线" x:CharSet="134" ss:Size="11" ss:Color="#000000"
    ss:Bold="1"/>
  </Style>
  <Style ss:ID="s86">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders/>
   <Font ss:FontName="等线" x:CharSet="134" ss:Size="11" ss:Color="#000000"
    ss:Bold="1"/>
  </Style>
  <Style ss:ID="s92">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center" ss:WrapText="1"/>
   <Font ss:FontName="等线" x:CharSet="134" ss:Size="16" ss:Color="#000000"
    ss:Bold="1"/>
  </Style>
  <Style ss:ID="s93">
   <Font ss:FontName="等线" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
  </Style>
 </Styles>
 <Worksheet ss:Name="Sheet1">
  <Table x:FullColumns="1"
   x:FullRows="1" ss:DefaultColumnWidth="53.25" ss:DefaultRowHeight="14.25">
   <Column ss:AutoFitWidth="0" ss:Width="110.25"/>
   <Column ss:AutoFitWidth="0" ss:Width="109.5" ss:Span="1"/>
   <Column ss:Index="4" ss:AutoFitWidth="0" ss:Width="102.75"/>
   <Column ss:AutoFitWidth="0" ss:Width="107.25" ss:Span="1"/>
   <Column ss:Index="7" ss:Width="90" ss:Span="1"/>
   <Column ss:Index="9" ss:Width="96.75"/>
   <Column ss:StyleID="s64" ss:AutoFitWidth="0" ss:Width="105"/>
   <Column ss:StyleID="s65" ss:AutoFitWidth="0" ss:Width="113.25"/>
   <Column ss:StyleID="s65" ss:AutoFitWidth="0" ss:Width="115.5"/>
   <Row ss:AutoFitHeight="0" ss:Height="13.5">
    <Cell ss:MergeAcross="${numlist?size+7}" ss:MergeDown="1" ss:StyleID="s92"><ss:Data
      ss:Type="String" xmlns="http://www.w3.org/TR/REC-html40"><B><Font
        html:Color="#000000">${month!''}份前湾库外包队劳务用工情况表</Font></B></ss:Data></Cell>
   </Row>
   <Row ss:AutoFitHeight="0" ss:Height="13.5"/>
   <Row>
    <Cell ss:MergeDown="1" ss:StyleID="s86"><Data ss:Type="String">序号</Data></Cell>
    <Cell ss:MergeDown="1" ss:StyleID="s86"><Data ss:Type="String">日期</Data></Cell>
    <Cell ss:MergeDown="1" ss:StyleID="s86"><Data ss:Type="String">库房</Data></Cell>
    <Cell ss:MergeDown="1" ss:StyleID="s86"><Data ss:Type="String">类别</Data></Cell>
    <Cell ss:MergeDown="1" ss:StyleID="s86"><Data ss:Type="String">单价</Data></Cell>
    <Cell ss:MergeDown="1" ss:StyleID="s86"><Data ss:Type="String">单位</Data></Cell>
	<#list custlist as cust>
		<Cell ss:MergeAcross="1" ss:StyleID="s83"><Data ss:Type="String">${cust!''}</Data></Cell>
	</#list>
    <Cell ss:MergeAcross="1" ss:StyleID="s83"><Data ss:Type="String">装卸队作业</Data></Cell>
   </Row>
   <Row>
		 <#list custlist as cust>
		   <#if cust_index==0>
			 <Cell ss:Index="7" ss:StyleID="s86"><Data ss:Type="String">作业量</Data></Cell>
			 <Cell ss:StyleID="s86"><Data ss:Type="String">金额</Data></Cell>
		   <#else>
			 <Cell ss:StyleID="s86"><Data ss:Type="String">作业量</Data></Cell>
			 <Cell ss:StyleID="s86"><Data ss:Type="String">金额</Data></Cell>
		   </#if>
		</#list>
	   <Cell ss:StyleID="s86"><Data ss:Type="String">作业量</Data></Cell>
	   <Cell ss:StyleID="s86"><Data ss:Type="String">金额</Data></Cell>
       <Cell ss:StyleID="s76"/>
       <Cell ss:StyleID="s76"/>
   </Row>
   <#list lxList as lx>
      <#assign size="${lx[2]!''}">
      <#list lxmap["${lx[3]!''}"] as mxlist>
	       <Row ss:AutoFitHeight="0" ss:Height="21">
			   <#if ((mxlist_index==0)&&((size?number) &gt; 1))>
					<Cell ss:MergeDown="${lx[2]}" ss:StyleID="s93"><Data ss:Type="Number">${lx_index+1}</Data></Cell>
					<Cell ss:MergeDown="${lx[2]}" ss:StyleID="s93"><Data ss:Type="String">${date!''}</Data></Cell>
					<Cell ss:MergeDown="${lx[2]}" ss:StyleID="s93"><Data ss:Type="String">${lx[0]!''}</Data></Cell>
					<Cell ss:StyleID="s93"><Data ss:Type="String">${mxlist[1]!''}</Data></Cell>
			   <#else>
			       <#if ((size?number)==1)>
						<Cell ss:StyleID="s93"><Data ss:Type="Number">${lx_index+1}</Data></Cell>
						<Cell ss:StyleID="s93"><Data ss:Type="String">${date!''}</Data></Cell>
						<Cell ss:StyleID="s93"><Data ss:Type="String">${lx[0]!''}</Data></Cell>
						<Cell ss:StyleID="s93"><Data ss:Type="String">${mxlist[1]!''}</Data></Cell>
				   <#else>
				        <Cell ss:Index="4" ss:StyleID="s93"><Data ss:Type="String">${mxlist[1]!''}</Data></Cell>
				   </#if>
			   </#if>
			<Cell ss:StyleID="s93"><Data ss:Type="String">${mxlist[2]!''}</Data></Cell>
			<Cell ss:StyleID="s93"><Data ss:Type="String">${mxlist[3]!''}</Data></Cell>
			<#list numlist as num>
			      <Cell ss:StyleID="s93"><Data ss:Type="String">${mxlist[num_index+4]!''}</Data></Cell>
			</#list>
			<Cell ss:StyleID="s93"><Data ss:Type="String">${mxlist[numlist?size+4]!''}</Data></Cell>
			<Cell ss:StyleID="s93"><Data ss:Type="String">${mxlist[numlist?size+5]!''}</Data></Cell>
		  </Row>
      </#list>
	      <#if ((size?number) &gt;1)>
		    <Row ss:AutoFitHeight="0" ss:Height="21">
				<Cell ss:Index="4" ss:StyleID="s93"><Data ss:Type="String">小计</Data></Cell>
				<Cell ss:StyleID="s93"><Data ss:Type="String">/</Data></Cell>
				<Cell ss:StyleID="s93"><Data ss:Type="String">${lx[1]!''}</Data></Cell>
				<#list numlist as num>
			      <Cell ss:StyleID="s93"><Data ss:Type="String">${lx[num_index+4]!''}</Data></Cell>
			    </#list>
				<Cell ss:StyleID="s93"><Data ss:Type="String">${lx[numlist?size+4]!''}</Data></Cell>
				<Cell ss:StyleID="s93"><Data ss:Type="String">${lx[numlist?size+5]!''}</Data></Cell>
		   </Row>
	     </#if>
   </#list>
   
   <Row ss:AutoFitHeight="0" ss:Height="24">
    <Cell ss:MergeAcross="5" ss:StyleID="s77"><Data ss:Type="String">合计：</Data></Cell>
	<#list sumList as sumlist>
		<#list numlist as num>
			 <Cell ss:StyleID="s93"><Data ss:Type="String">${sumlist[num_index]!''}</Data></Cell>
		</#list>
		<Cell ss:StyleID="s93"><Data ss:Type="String">${sumlist[numlist?size]!''}</Data></Cell>
	    <Cell ss:StyleID="s93"><Data ss:Type="String">${sumlist[numlist?size+1]!''}</Data></Cell>
	</#list>
   </Row>
   <Row ss:AutoFitHeight="0" ss:Height="26.25">
    <Cell ss:MergeAcross="5" ss:StyleID="s93"/>
   </Row>
   <Row ss:AutoFitHeight="0" ss:Height="23.25">
    <Cell ss:StyleID="s93"><Data ss:Type="String">制表人：${user}</Data></Cell>
    <Cell ss:Index="3" ss:StyleID="s93"><Data ss:Type="String">操作部：</Data></Cell>
    <Cell ss:Index="5" ss:StyleID="s93"><Data ss:Type="String">财务部：</Data></Cell>
    <Cell ss:Index="7" ss:StyleID="s93"><Data ss:Type="String">中心领导：</Data></Cell>
   </Row>
  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <PageSetup>
    <Layout x:Orientation="Landscape"/>
    <Header x:Margin="0.3"/>
    <Footer x:Margin="0.3"/>
    <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
   </PageSetup>
   <Print>
    <ValidPrinterInfo/>
    <PaperSizeIndex>9</PaperSizeIndex>
    <HorizontalResolution>300</HorizontalResolution>
    <VerticalResolution>300</VerticalResolution>
   </Print>
   <PageBreakZoom>60</PageBreakZoom>
   <Selected/>
   <TopRowVisible>1</TopRowVisible>
   <Panes>
    <Pane>
     <Number>3</Number>
     <ActiveRow>9</ActiveRow>
     <ActiveCol>8</ActiveCol>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
</Workbook>
