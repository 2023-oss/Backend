package com.project.easysign.exception;

public class NonExistentRegionException extends CustomSignException {
    private static final String MESSAGE = "해당 지역에 대한 데이터는 존재하지 않습니다.";
    public NonExistentRegionException(){
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
