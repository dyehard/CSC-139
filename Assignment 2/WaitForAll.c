int SqFindProd(int size) {
	int productResult = gData[0];
	for (int i = 1; i < size; i++){
		if(gData[i] == 0)
			break;

		productResult *= gData[i];
		productResult %= NUM_LIMIT;
	}
	return productResult;
}